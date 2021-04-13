package lbw.srb.core.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lbw.srb.common.exception.Assert;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.core.enums.LendStatusEnum;
import lbw.srb.core.enums.ReturnMethodEnum;
import lbw.srb.core.enums.TransTypeEnum;
import lbw.srb.core.hfb.FormHelper;
import lbw.srb.core.hfb.HfbConst;
import lbw.srb.core.hfb.RequestHelper;
import lbw.srb.core.mapper.LendMapper;
import lbw.srb.core.mapper.LendReturnMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.mapper.UserIntegralMapper;
import lbw.srb.core.pojo.entity.*;
import lbw.srb.core.service.*;
import lbw.srb.core.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.awt.windows.WWindowPeer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务实现类
 * </p>
 */
@Service
@Slf4j
public class LendReturnServiceImpl extends ServiceImpl<LendReturnMapper, LendReturn> implements LendReturnService {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private LendMapper lendMapper;
    @Autowired
    private LendItemReturnService lendItemReturnService;
    @Autowired
    private UserBindService userBindService;
    @Autowired
    private TransFlowService transFlowService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LendItemService lendItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String HQnotify(Map<String, Object> paramMap) {
        String agentBatchNo = (String)paramMap.get("agentBatchNo");
//        手续费
        BigDecimal voteFeeAmt = new BigDecimal((String)paramMap.get("voteFeeAmt"));
//        扣款额
        BigDecimal totalAmt = new BigDecimal((String)paramMap.get("totalAmt"));

        if (transFlowService.isExist(agentBatchNo)){
            log.error("还钱出错,单号已存在"+paramMap);
            return "fail";
        }
        QueryWrapper<LendReturn> wrapper = new QueryWrapper<>();
        wrapper.eq("return_no",agentBatchNo);
        LendReturn lendReturn = baseMapper.selectOne(wrapper);

        UserAccount userAccount = userAccountService.getByUserId(lendReturn.getUserId());
        Assert.isTrue(userAccount.getAmount().doubleValue() >= lendReturn.getTotal().doubleValue(),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        lendReturn.setFee(voteFeeAmt);
        lendReturn.setRealReturnTime(LocalDateTime.now());
        lendReturn.setStatus(1);
        baseMapper.updateById(lendReturn);

//        记录是不是最后一次
        boolean flag=false;
        //        是最后一次
        if (lendReturn.getLast()){
            Lend lend = lendMapper.selectById(lendReturn.getLendId());
            lend.setStatus(LendStatusEnum.PAY_OK.getStatus());
            lendMapper.updateById(lend);
            flag=true;
//            加分
            userInfoService.AddIntegral(lend.getUserId(),lend.getAmount().divideToIntegralValue(new BigDecimal(1000)).intValue(),"完成还款");
        }
//        扣钱
        userAccount.setAmount(userAccount.getAmount().subtract(totalAmt));
        userAccountService.updateById(userAccount);
//        流水
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userAccount.getUserId());
        transFlow.setUserName(userInfoService.getNameById(userAccount.getUserId()));
        transFlow.setTransNo(agentBatchNo);
        transFlow.setTransType(TransTypeEnum.RETURN_DOWN.getTransType());
        transFlow.setTransTypeName(TransTypeEnum.RETURN_DOWN.getTransTypeName());
        transFlow.setTransAmount(totalAmt);
        transFlowService.save(transFlow);

//        投资人
        QueryWrapper<LendItemReturn> lendItemReturnQueryWrapper = new QueryWrapper<>();
        lendItemReturnQueryWrapper
                .eq("lend_id",lendReturn.getLendId())
                .eq("current_period",lendReturn.getCurrentPeriod());
        List<LendItemReturn> lendItemReturns = lendItemReturnService.list(lendItemReturnQueryWrapper);
        for (LendItemReturn lendItemReturn : lendItemReturns) {
//            回款计划
            lendItemReturn.setRealReturnTime(LocalDateTime.now());
            lendItemReturn.setStatus(1);
            lendItemReturnService.updateById(lendItemReturn);

            LendItem lendItem = lendItemService.getById(lendItemReturn.getLendItemId());
            lendItem.setRealAmount(lendItem.getRealAmount().add(lendItemReturn.getInvestAmount()));
//            是最后一次
            if (flag)
                lendItem.setStatus(2);
            lendItemService.updateById(lendItem);

//            给投资人发钱
            UpdateWrapper<UserAccount> userAccountUpdateWrapper = new UpdateWrapper<>();
            userAccountUpdateWrapper.setSql("amount=amount+"+lendItemReturn.getTotal())
                    .eq("user_id",lendItemReturn.getInvestUserId());
            userAccountService.update(null,userAccountUpdateWrapper);

//            发钱流水
            TransFlow tf = new TransFlow();
            tf.setUserId(lendItemReturn.getInvestUserId());
            tf.setUserName(userInfoService.getNameById(lendItemReturn.getInvestUserId()));
            tf.setTransNo(LendNoUtils.getReturnItemNo());
            tf.setTransTypeName(TransTypeEnum.INVEST_BACK.getTransTypeName());
            tf.setTransType(TransTypeEnum.BORROW_BACK.getTransType());
            tf.setTransAmount(lendItemReturn.getTotal());
            transFlowService.save(tf);
        }
        return "success";
    }


    @Override
    public String commitReturn(Long lendReturnId, Long userId) {
        //还款记录
        LendReturn lendReturn = baseMapper.selectById(lendReturnId);

        //获取用户余额
        BigDecimal amount = userAccountService.getAmount(userId);
        Assert.isTrue(amount.doubleValue() >= lendReturn.getTotal().doubleValue(),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        //标的记录
        Lend lend = lendMapper.selectById(lendReturn.getLendId());
        //获取还款人的绑定号
        String bindCode = userBindService.getBindCodeByUserId(userId);

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        //商户商品名称
        paramMap.put("agentGoodsName", lend.getTitle());
        //批次号
        paramMap.put("agentBatchNo", lendReturn.getReturnNo());
        //还款人绑定协议号
        paramMap.put("fromBindCode", bindCode);
        //还款总额
        paramMap.put("totalAmt", lendReturn.getTotal());
        paramMap.put("note", "");
        //还款明细
        List<Map<String, Object>> lendItemReturnDetailList = lendItemReturnService.addReturnDetail(lendReturnId);
        paramMap.put("data", JSONObject.toJSONString(lendItemReturnDetailList));

        paramMap.put("voteFeeAmt", new BigDecimal(0));
        paramMap.put("notifyUrl", HfbConst.BORROW_RETURN_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.BORROW_RETURN_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        //构建自动提交表单
        return FormHelper.buildForm(HfbConst.BORROW_RETURN_URL, paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    生成还款计划
    public ArrayList<LendReturn> repaymentPlan(Lend lend) {
        ArrayList<LendReturn> lendReturns = new ArrayList<>();
        LendReturn baseReturn = new LendReturn();
        baseReturn.setLendId(lend.getId());
        baseReturn.setBorrowInfoId(lend.getBorrowInfoId());
        baseReturn.setUserId(lend.getUserId());
        baseReturn.setAmount(lend.getAmount());
        baseReturn.setLendYearRate(lend.getLendYearRate());
        baseReturn.setReturnMethod(lend.getReturnMethod());
        baseReturn.setOverdue(false);
        baseReturn.setLast(false);
        baseReturn.setStatus(0);
        baseReturn.setBaseAmount(lend.getInvestAmount());

        Map<Integer, BigDecimal> mapInterest = null;  //还款期数 -> 利息
        Map<Integer, BigDecimal> mapPrincipal = null; //还款期数 -> 本金

        //根据还款方式计算本金和利息
        if (lend.getReturnMethod().intValue() == ReturnMethodEnum.ONE.getMethod()) {
            //利息
            mapInterest = Amount1Helper.getPerMonthInterest(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
            //本金
            mapPrincipal = Amount1Helper.getPerMonthPrincipal(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
        } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.TWO.getMethod()) {
            mapInterest = Amount2Helper.getPerMonthInterest(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
            mapPrincipal = Amount2Helper.getPerMonthPrincipal(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
        } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.THREE.getMethod()) {
            mapInterest = Amount3Helper.getPerMonthInterest(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
            mapPrincipal = Amount3Helper.getPerMonthPrincipal(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
        } else {
            mapInterest = Amount4Helper.getPerMonthInterest(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
            mapPrincipal = Amount4Helper.getPerMonthPrincipal(lend.getAmount(), lend.getLendYearRate(), lend.getPeriod());
        }

        for (int i = 1; i <= lend.getPeriod(); i++) {
            LendReturn lendReturn = new LendReturn();
            BeanUtils.copyProperties(baseReturn,lendReturn);
            lendReturn.setReturnNo(LendNoUtils.getReturnNo());
            lendReturn.setCurrentPeriod(i);
            lendReturn.setReturnDate(lend.getLendStartDate().plusMonths(i));
            if (i==lend.getPeriod())
                lendReturn.setLast(true);
            lendReturn.setPrincipal(mapPrincipal.get(i));
            lendReturn.setInterest(mapInterest.get(i));
            lendReturn.setTotal(mapPrincipal.get(i).add(mapInterest.get(i)));
            lendReturns.add(lendReturn);
        }
        saveBatch(lendReturns);
        return lendReturns;
    }

    @Override
    public List<LendReturn> findAllByLendId(Long lendId) {
        QueryWrapper<LendReturn> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_id",lendId);
        return baseMapper.selectList(wrapper);
    }
}
