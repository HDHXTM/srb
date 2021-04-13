package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.common.exception.Assert;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.core.enums.LendStatusEnum;
import lbw.srb.core.enums.TransTypeEnum;
import lbw.srb.core.hfb.FormHelper;
import lbw.srb.core.hfb.HfbConst;
import lbw.srb.core.hfb.RequestHelper;
import lbw.srb.core.mapper.LendItemMapper;
import lbw.srb.core.mapper.LendMapper;
import lbw.srb.core.mapper.UserAccountMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.pojo.entity.LendItem;
import lbw.srb.core.pojo.entity.TransFlow;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.vo.InvestVO;
import lbw.srb.core.service.*;
import lbw.srb.core.util.LendNoUtils;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 */
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {

    @Autowired
    private LendService lendService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TransFlowService transFlowService;
    @Autowired
    private UserBindService userBindService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String TZnotify(Map<String, Object> check) {
        String agentBillNo = (String) check.get("agentBillNo");
        BigDecimal voteAmt = new BigDecimal((String)check.get("voteAmt"));
        if(transFlowService.isExist(agentBillNo)){
            log.error("订单已存在"+check);
            return "fail";
        }
        LendItem lendItem = findByLendNo(agentBillNo);
        Lend lend = lendService.getById(lendItem.getLendId());

//        再看一次满没满,满了应该返回fail,让汇付宝回滚数据
        //        已完成的不能投
        if(!Objects.equals(lend.getStatus(), LendStatusEnum.INVEST_RUN.getStatus()))
            return "fail";
        //超卖：已投金额 + 当前投资金额 <= 标的金额（正常）
        BigDecimal sum = lend.getInvestAmount().add(voteAmt);
        if (sum.doubleValue() > lend.getAmount().doubleValue())
            return "fail";
        lend.setInvestAmount(sum);
        lend.setInvestNum(lend.getInvestNum()+1);

//        钱够了
        if(lend.getInvestAmount().compareTo(lend.getAmount())!=-1){
            lend.setStatus(LendStatusEnum.FULL.getStatus());
//            应该放款才计息
//            LocalDate start = LocalDate.now();
//            LocalDate end = start.plusMonths(lend.getPeriod());
//            lend.setLendStartDate(start);
//            lend.setLendEndDate(end);
//            setLendStartDateByLendId(lend.getId(),start,end);
        }
        lendService.updateById(lend);

        lendItem.setStatus(1);
        lendItem.setRealAmount(new BigDecimal(0));
        baseMapper.updateById(lendItem);

        UserInfo userInfo = userInfoService.getById(lendItem.getInvestUserId());


//        冻结钱
        userAccountService.freeze(userInfo.getId(),voteAmt);

        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(lendItem.getLendItemNo());
        transFlow.setTransType(TransTypeEnum.INVEST_LOCK.getTransType());
        transFlow.setTransTypeName(TransTypeEnum.INVEST_LOCK.getTransTypeName());
        transFlow.setTransAmount(voteAmt);
        transFlowService.save(transFlow);
        return "success";
    }

//同步设置
    @Transactional(rollbackFor = Exception.class)
    public void setLendItemStartDateByLendId(Long id, LocalDate start, LocalDate end) {
        QueryWrapper<LendItem> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_id",id);
        List<LendItem> lendItems = baseMapper.selectList(wrapper);
        for (LendItem lendItem : lendItems) {
            lendItem.setLendStartDate(start);
            lendItem.setLendEndDate(end);
        }
        updateBatchById(lendItems);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String invest(InvestVO investVO, Long userId) {
        Lend lend = lendService.getById(investVO.getLendId());
//        已完成的不能投
        Assert.equals(lend.getStatus(),LendStatusEnum.INVEST_RUN.getStatus(), ResponseEnum.LEND_INVEST_ERROR);
//        钱不够，不能投
        BigDecimal amount = userAccountService.getAmount(userId);
        Assert.isTrue(amount.doubleValue()>= investVO.getInvestAmount().doubleValue(),ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        //超卖：已投金额 + 当前投资金额 <= 标的金额（正常）
        BigDecimal sum = lend.getInvestAmount().add(investVO.getInvestAmount());
        Assert.isTrue(
                sum.doubleValue() <= lend.getAmount().doubleValue(),
                ResponseEnum.LEND_FULL_SCALE_ERROR);


        LendItem lendItem = new LendItem();
        lendItem.setLendItemNo(LendNoUtils.getLendItemNo());
        lendItem.setLendId(investVO.getLendId());
        lendItem.setInvestUserId(userId);
        lendItem.setInvestName(userInfoService.getNameById(userId));
        lendItem.setInvestAmount(investVO.getInvestAmount());
        lendItem.setLendYearRate(lend.getLendYearRate());
        lendItem.setExpectAmount(lendService.getInterestCount(
                investVO.getInvestAmount(),
                lend.getLendYearRate(),
                lend.getPeriod(),
                lend.getReturnMethod()));
//        还没确认扣钱
        lendItem.setStatus(0);
        baseMapper.insert(lendItem);

//获取投资人的bindCode
        String bindCode = userBindService.getBindCodeByUserId(userId);

        //获取借款人的bindCode
        String benefitBindCode = userBindService.getBindCodeByUserId(lend.getUserId());

        //封装提交至汇付宝的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("voteBindCode", bindCode);
        paramMap.put("benefitBindCode",benefitBindCode);
        paramMap.put("agentProjectCode", lend.getLendNo());//项目标号
        paramMap.put("agentProjectName", lend.getTitle());
        paramMap.put("agentBillNo", lendItem.getLendItemNo());//订单编号
        paramMap.put("voteAmt", investVO.getInvestAmount());
        paramMap.put("votePrizeAmt", "0");
        paramMap.put("voteFeeAmt", "0");
        paramMap.put("projectAmt", lend.getAmount()); //标的总金额
        paramMap.put("note", "");
        paramMap.put("notifyUrl", HfbConst.INVEST_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.INVEST_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        //构建充值自动提交表单
        return FormHelper.buildForm(HfbConst.INVEST_URL, paramMap);
    }

    @Override
    public List<LendItem> findAllByLendId(Long lendId) {
        QueryWrapper<LendItem> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_id",lendId).ne("status",0);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public LendItem findByLendNo(String lendNO) {
        QueryWrapper<LendItem> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_item_no",lendNO);
        return baseMapper.selectOne(wrapper);

    }
}
