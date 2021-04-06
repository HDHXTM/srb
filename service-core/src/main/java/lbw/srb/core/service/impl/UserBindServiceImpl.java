package lbw.srb.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.UserBindMapper;
import lbw.srb.core.pojo.entity.UserBind;
import lbw.srb.core.service.UserBindService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

}
