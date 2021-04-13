package lbw.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.query.UserInfoQuery;
import lbw.srb.core.pojo.vo.LoginVO;
import lbw.srb.core.pojo.vo.RegisterVO;
import lbw.srb.core.pojo.vo.UserIndexVO;
import lbw.srb.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO,String ip);

    UserIndexVO getIndexUserInfo(Long userId);

    IPage<UserInfo> search(Page<UserInfo> userInfoPage, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    String adminLogin(String username, String password,String ip);

    String getNameById(Long id);

    void AddIntegral(Long userId,Integer integral,String content);
}
