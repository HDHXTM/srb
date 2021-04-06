package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lbw.srb.common.util.MD5;
import lbw.srb.core.mapper.UserAccountMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.pojo.entity.UserAccount;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.vo.LoginVO;
import lbw.srb.core.pojo.vo.RegisterVO;
import lbw.srb.core.pojo.vo.UserInfoVO;
import lbw.srb.core.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserAccountMapper userAccountMapper;

    @Override
    public UserInfoVO login(LoginVO loginVO) {
        return null;
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
}
