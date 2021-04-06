package lbw.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.vo.LoginVO;
import lbw.srb.core.pojo.vo.RegisterVO;
import lbw.srb.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO);
}
