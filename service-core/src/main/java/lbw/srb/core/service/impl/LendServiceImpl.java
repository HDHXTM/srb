package lbw.srb.core.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.common.exception.BusinessException;
import lbw.srb.core.enums.BorrowerStatusEnum;
import lbw.srb.core.enums.LendStatusEnum;
import lbw.srb.core.enums.ReturnMethodEnum;
import lbw.srb.core.enums.TransTypeEnum;
import lbw.srb.core.hfb.HfbConst;
import lbw.srb.core.hfb.RequestHelper;
import lbw.srb.core.mapper.BorrowerMapper;
import lbw.srb.core.mapper.LendMapper;
import lbw.srb.core.mapper.UserAccountMapper;
import lbw.srb.core.pojo.entity.*;
import lbw.srb.core.pojo.vo.BorrowInfoApprovalVO;
import lbw.srb.core.pojo.vo.BorrowerDetailVO;
import lbw.srb.core.service.*;
import lbw.srb.core.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 */
@Service
@Slf4j
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Autowired
    private DictService dictService;
    @Autowired
    private BorrowerService borrowerService;
    @Autowired
    private LendItemService lendItemService;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TransFlowService transFlowService;
    @Autowired
    private LendReturnService lendReturnService;
    @Autowired
    private LendItemReturnService lendItemReturnService;

    @Override
    public List<Lend> findAll() {
        List<Lend> lends = baseMapper.selectList(null);
        for (Lend lend : lends) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("returnMethod", dictService.findNameByCodeAndValue("returnMethod", lend.getReturnMethod()));
            map.put("status", LendStatusEnum.getMsgByStatus(lend.getStatus()));
            lend.setParam(map);
        }
        return lends;
    }


    @Override
    public BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalmonth, Integer returnMethod) {

        BigDecimal interestCount;
        if (returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()) {
            interestCount = Amount1Helper.getInterestCount(invest, yearRate, totalmonth);
        } else if (returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()) {
            interestCount = Amount2Helper.getInterestCount(invest, yearRate, totalmonth);
        } else if (returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()) {
            interestCount = Amount3Helper.getInterestCount(invest, yearRate, totalmonth);
        } else {
            interestCount = Amount4Helper.getInterestCount(invest, yearRate, totalmonth);
        }
        return interestCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeLoan(Long id,Long adminId) {
        Lend lend = baseMapper.selectById(id);

        //调用汇付宝放款接口
        Map<String, Object> map = new HashMap<>();
        map.put("agentId", HfbConst.AGENT_ID);
        map.put("agentProjectCode", lend.getLendNo());
        map.put("agentBillNo", LendNoUtils.getLoanNo());

        //月年化
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        //平台服务费 = 已投金额 * 月年化 * 投资时长
        BigDecimal realAmount = lend.getInvestAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        map.put("mchFee", realAmount);

        map.put("timestamp", RequestHelper.getTimestamp());
        map.put("sign", RequestHelper.getSign(map));

        //提交远程请求
        JSONObject result = RequestHelper.sendRequest(map, HfbConst.MAKE_LOAD_URL);
        log.info("放款结果：" + result.toJSONString());

        //放款失败
        if (!"0000".equals(result.getString("resultCode")))
            throw new BusinessException(result.getString("resultMsg"));

//        更新标的
        lend.setRealAmount(realAmount);
        LocalDate start = LocalDate.now().plusMonths(1);
        LocalDate end = start.plusMonths(lend.getPeriod());
        lend.setLendStartDate(start);
        lend.setLendEndDate(end);
        lend.setStatus(LendStatusEnum.PAY_RUN.getStatus());
        lend.setPaymentAdminId(adminId);
        lend.setPaymentTime(LocalDateTime.now());
        baseMapper.updateById(lend);

        lendItemService.setLendItemStartDateByLendId(lend.getId(), start, end);

//        发钱
        QueryWrapper<UserAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",lend.getUserId());
        UserAccount userAccount = userAccountMapper.selectOne(wrapper);
        userAccount.setAmount(userAccount.getAmount().add(new BigDecimal(result.getString("voteAmt"))));
        userAccountMapper.updateById(userAccount);
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(lend.getUserId());
        transFlow.setUserName(userInfoService.getNameById(lend.getUserId()));
        transFlow.setTransNo(result.getString("agentBillNo"));
        transFlow.setTransType(TransTypeEnum.BORROW_BACK.getTransType());
        transFlow.setTransTypeName(TransTypeEnum.BORROW_BACK.getTransTypeName());
        transFlow.setTransAmount(lend.getAmount());
        transFlowService.save(transFlow);

//        解冻，扣钱
        userAccountService.unFreezeByLendItems(lendItemService.findAllByLendId(lend.getId()));

        //        生成还款计划
        ArrayList<LendReturn> lendReturns = lendReturnService.repaymentPlan(lend);

//        生成每一期的收钱计划
        lendItemReturnService.repaymentPlan(lend,lendReturns);
    }


    @Override
    public Map<String, Object> getLendDetail(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        Lend lend = baseMapper.selectById(id);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("returnMethod", dictService.findNameByCodeAndValue("returnMethod", lend.getReturnMethod()));
        map2.put("status", LendStatusEnum.getMsgByStatus(lend.getStatus()));
        lend.setParam(map2);
        map.put("lend", lend);

        map.put("borrower", borrowerService.getDetailByUserId(lend.getUserId()));
        return map;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo, Long userId) {
        Lend lend = new Lend();
        BeanUtils.copyProperties(borrowInfo, lend);
        lend.setId(null);
        lend.setCreateTime(null);
        lend.setUpdateTime(null);
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());

        lend.setBorrowInfoId(borrowInfo.getId());
//        不是有id吗，这啥玩意
        lend.setLendNo(LendNoUtils.getLendNo());
        lend.setLowestAmount(new BigDecimal(100)); //最低投资金额
        lend.setInvestAmount(new BigDecimal(0)); //已投金额
        lend.setInvestNum(0); //已投人数
        //平台预期收益 = 标的金额 * (平台服务费率 / 12 * 期数)
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate.multiply(new BigDecimal(lend.getPeriod())));
        lend.setExpectAmount(expectAmount);

        lend.setCheckAdminId(userId);

        baseMapper.insert(lend);
    }
}
