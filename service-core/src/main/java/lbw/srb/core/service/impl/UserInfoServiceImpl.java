package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lbw.srb.common.exception.Assert;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.common.util.JwtUtils;
import lbw.srb.common.util.MD5;
import lbw.srb.core.mapper.UserAccountMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.mapper.UserLoginRecordMapper;
import lbw.srb.core.pojo.entity.UserAccount;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.entity.UserLoginRecord;
import lbw.srb.core.pojo.query.UserInfoQuery;
import lbw.srb.core.pojo.vo.LoginVO;
import lbw.srb.core.pojo.vo.RegisterVO;
import lbw.srb.core.pojo.vo.UserIndexVO;
import lbw.srb.core.pojo.vo.UserInfoVO;
import lbw.srb.core.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserLoginRecordMapper userLoginRecordMapper;

    @Override
    @Transactional
    public UserInfoVO login(LoginVO loginVO,String ip) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",loginVO.getMobile());
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);
        Assert.equals(userInfo.getStatus(),1,ResponseEnum.LOGIN_LOKED_ERROR);
        Assert.equals(userInfo.getPassword(), MD5.encrypt(loginVO.getPassword()),ResponseEnum.LOGIN_PASSWORD_ERROR);
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName(), userInfo.getUserType());
        userLoginRecordMapper.insert(new UserLoginRecord(userInfo.getId(), ip));
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo,userInfoVO);
        userInfoVO.setToken(token);
        return userInfoVO;
    }

    @Override
    public void register(RegisterVO registerVO) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(registerVO, userInfo);
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
        userInfo.setHeadImg(UserInfo.USER_AVATAR);
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        baseMapper.insert(userInfo);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);
    }

    @Override
    @Cacheable(value = "userInfo",key = "#userId")
    public UserIndexVO getIndexUserInfo(Long userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        UserIndexVO userIndexVO = new UserIndexVO();
        BeanUtils.copyProperties(userInfo,userIndexVO);
        userIndexVO.setUserId(userId);
        QueryWrapper<UserLoginRecord> wrapper = new QueryWrapper<>();
        wrapper
                .eq("user_id",userId)
                .orderByDesc("create_time")
                .last("limit 1");
        UserLoginRecord record = userLoginRecordMapper.selectOne(wrapper);
        userIndexVO.setLastLoginTime(record.getCreateTime());
        QueryWrapper<UserAccount> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id",userId);
        UserAccount userAccount = userAccountMapper.selectOne(wrapper1);
        userIndexVO.setAmount(userAccount.getAmount());
        userIndexVO.setFreezeAmount(userAccount.getFreezeAmount());
        return userIndexVO;
    }

    @Override
    public IPage<UserInfo> search(Page<UserInfo> userInfoPage, UserInfoQuery userInfoQuery) {
        if (userInfoQuery==null)
            return baseMapper.selectPage(userInfoPage,null);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(userInfoQuery.getMobile()),"mobile", userInfoQuery.getMobile())
                .eq(userInfoQuery.getStatus()!=null,"status",userInfoQuery.getStatus())
                .eq(userInfoQuery.getUserType()!=null,"user_type",userInfoQuery.getUserType());
        return baseMapper.selectPage(userInfoPage,wrapper);
    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }
}
