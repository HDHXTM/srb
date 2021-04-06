package lbw.srb.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.UserLoginRecordMapper;
import lbw.srb.core.pojo.entity.UserLoginRecord;
import lbw.srb.core.service.UserLoginRecordService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

}
