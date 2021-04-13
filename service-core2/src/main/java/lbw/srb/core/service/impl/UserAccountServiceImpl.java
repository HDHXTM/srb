package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.common.exception.Assert;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.core.enums.TransTypeEnum;
import lbw.srb.core.hfb.FormHelper;
import lbw.srb.core.hfb.HfbConst;
import lbw.srb.core.hfb.RequestHelper;
import lbw.srb.core.mapper.UserAccountMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.pojo.entity.LendItem;
import lbw.srb.core.pojo.entity.TransFlow;
import lbw.srb.core.pojo.entity.UserAccount;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.service.TransFlowService;
import lbw.srb.core.service.UserAccountService;
import lbw.srb.core.service.UserBindService;
import lbw.srb.core.service.UserInfoService;
import lbw.srb.core.util.LendNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TransFlowService transFlowService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserBindService userBindService;



    @Override
    public BigDecimal getAmount(Long userId) {
        QueryWrapper<UserAccount> wrapper = new QueryWrapper<>();
        wrapper.select("amount").eq("user_id",userId);
        return (BigDecimal)baseMapper.selectObjs(wrapper).get(0);
    }

    @Override
    public String withdraw(BigDecimal fetchAmt, Long userId) {
        //用户账户余额

        BigDecimal amount = getAmount(userId);
        Assert.isTrue(amount.doubleValue() >= fetchAmt.doubleValue(),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);


        String bindCode = userBindService.getBindCodeByUserId(userId);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getWithdrawNo());
        paramMap.put("bindCode", bindCode);
        paramMap.put("fetchAmt", fetchAmt);
        paramMap.put("feeAmt", new BigDecimal(0));
        paramMap.put("notifyUrl", HfbConst.WITHDRAW_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.WITHDRAW_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        return FormHelper.buildForm(HfbConst.WITHDRAW_URL, paramMap);
    }


    @Override
    public UserAccount getByUserId(Long userId) {
        QueryWrapper<UserAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    根据借钱数据，清除冻结金额
    public void unFreezeByLendItems(List<LendItem> allByLendId) {
        for (LendItem lendItem : allByLendId) {
            UpdateWrapper<UserAccount> wrapper = new UpdateWrapper<>();
            wrapper.setSql("freeze_amount=freeze_amount-"+lendItem.getInvestAmount())
                    .eq("user_id",lendItem.getInvestUserId());
            baseMapper.update(null,wrapper);
            TransFlow transFlow = new TransFlow();
            transFlow.setTransAmount(lendItem.getInvestAmount());
            transFlow.setUserId(lendItem.getInvestUserId());
            transFlow.setUserName(userInfoService.getNameById(lendItem.getInvestUserId()));
            transFlow.setTransNo(LendNoUtils.getTransNo());
            transFlow.setTransType(TransTypeEnum.INVEST_UNLOCK.getTransType());
            transFlow.setTransTypeName(TransTypeEnum.INVEST_UNLOCK.getTransTypeName());
            transFlowService.save(transFlow);
        }
    }

    @Override
    public void freeze(Long id, BigDecimal voteAmt) {
        QueryWrapper<UserAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        UserAccount userAccount = baseMapper.selectOne(wrapper);
        userAccount.setAmount(userAccount.getAmount().subtract(voteAmt));
        userAccount.setFreezeAmount(userAccount.getFreezeAmount().add(voteAmt));
        baseMapper.updateById(userAccount);
    }

    @Override
    public String QQnotify(Map<String, Object> paramMap) {
        String agentBillNo = (String)paramMap.get("agentBillNo");
        if(transFlowService.isExist(agentBillNo)){
            log.error("订单已存在"+paramMap);
            return "fail";
        }

        String bindCode = (String)paramMap.get("bindCode");
        String fetchAmt = (String)paramMap.get("fetchAmt");
        //扣钱
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("bind_code",bindCode);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        UpdateWrapper<UserAccount> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("amount=amount-"+fetchAmt).eq("user_id", userInfo.getId());
        baseMapper.update(null,updateWrapper);

//        加流水记录
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(agentBillNo);
        transFlow.setTransType(TransTypeEnum.WITHDRAW.getTransType());
        transFlow.setTransTypeName(TransTypeEnum.WITHDRAW.getTransTypeName());
        transFlow.setTransAmount(new BigDecimal(fetchAmt));
        transFlowService.save(transFlow);

        return "success";

    }
    @Override
//    充钱回调
    public String CQnotify(Map<String, Object> paramMap) {
        String agentBillNo = (String)paramMap.get("agentBillNo");
        if(transFlowService.isExist(agentBillNo)){
            log.error("订单已存在"+paramMap);
            return "fail";
        }

        String bindCode = (String)paramMap.get("bindCode");
        String chargeAmt = (String)paramMap.get("chargeAmt");

        //加钱
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("bind_code",bindCode);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        UpdateWrapper<UserAccount> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("amount=amount+"+chargeAmt).eq("user_id", userInfo.getId());
        baseMapper.update(null,updateWrapper);

//        加流水记录
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(agentBillNo);
        transFlow.setTransType(TransTypeEnum.RECHARGE.getTransType());
        transFlow.setTransTypeName(TransTypeEnum.RECHARGE.getTransTypeName());
        transFlow.setTransAmount(new BigDecimal(chargeAmt));
        transFlowService.save(transFlow);


        return "success";
    }

    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        UserInfo userInfo = userInfoService.getById(userId);
        //组装自动提交表单的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getChargeNo());
        paramMap.put("bindCode", userInfo.getBindCode());
        paramMap.put("chargeAmt", chargeAmt);
        paramMap.put("feeAmt", 0);
        paramMap.put("notifyUrl", HfbConst.RECHARGE_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.RECHARGE_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        //生成动态表单字符串
        return FormHelper.buildForm(HfbConst.RECHARGE_URL, paramMap);
    }
}
