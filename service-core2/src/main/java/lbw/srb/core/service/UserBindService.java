package lbw.srb.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.UserBind;
import lbw.srb.core.pojo.vo.UserBindVO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 */
public interface UserBindService extends IService<UserBind> {

    String bind(UserBindVO userBindVO, Long userId);

    String notify(Map<String, Object> paramMap);

    String getBindCodeByUserId(Long userId);
}
