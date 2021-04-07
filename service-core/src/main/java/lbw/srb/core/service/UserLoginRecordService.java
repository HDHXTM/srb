package lbw.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.UserLoginRecord;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    List<UserLoginRecord> getTop50(Long userId);
}
