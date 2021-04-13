package lbw.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.common.exception.Assert;
import lbw.srb.common.exception.BusinessException;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.common.util.RedisUtil;
import lbw.srb.core.enums.UserBindEnum;
import lbw.srb.core.hfb.FormHelper;
import lbw.srb.core.hfb.HfbConst;
import lbw.srb.core.hfb.RequestHelper;
import lbw.srb.core.mapper.UserBindMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.pojo.entity.UserBind;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.vo.UserBindVO;
import lbw.srb.core.service.UserBindService;
import lbw.srb.core.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public String getBindCodeByUserId(Long userId) {
        QueryWrapper<UserBind> wrapper = new QueryWrapper<>();
        wrapper.select("bind_code").eq("user_id",userId);
        return (String)baseMapper.selectObjs(wrapper).get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String notify(Map<String, Object> paramMap) {
        String resultCode = (String) paramMap.get("resultCode");
        String bindCode = (String) paramMap.get("bindCode");
        String agentUserId = (String) paramMap.get("agentUserId");

        QueryWrapper<UserBind> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", agentUserId);

//        绑定不成功
        if (!"0001".equals(resultCode)|| StringUtils.checkValNull(resultCode)) {
            baseMapper.delete(wrapper);
            return "fail";
        }
//        成功
        UserBind userBind = baseMapper.selectOne(wrapper);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        userBind.setBindCode(bindCode);
        baseMapper.updateById(userBind);

//        更新用户信息
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindCode(userBind.getBindCode());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);

        return "success";
    }


    @Override
    public String bind(UserBindVO userBindVO, Long userId) {
        QueryWrapper<UserBind> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", userBindVO.getIdCard())
                .or()
                .eq("bank_no", userBindVO.getBankNo());
        if (baseMapper.selectCount(wrapper) > 0)
            throw new BusinessException("银行卡号或身份证号已被其他账号绑定", 250);

        UserBind userBind = new UserBind();
        BeanUtils.copyProperties(userBindVO, userBind);
        userBind.setUserId(userId);
        userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
        baseMapper.insert(userBind);

        //组装自动提交表单的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard", userBindVO.getIdCard());
        paramMap.put("personalName", userBindVO.getName());
        paramMap.put("bankType", userBindVO.getBankType());
        paramMap.put("bankNo", userBindVO.getBankNo());
        paramMap.put("mobile", userBindVO.getMobile());
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        //生成动态表单字符串
        return FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);
    }
}
