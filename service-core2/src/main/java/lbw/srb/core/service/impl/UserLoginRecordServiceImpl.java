package lbw.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.UserLoginRecordMapper;
import lbw.srb.core.pojo.entity.UserLoginRecord;
import lbw.srb.core.service.UserLoginRecordService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

    @Override
    public List<UserLoginRecord> getTop50(Long userId) {
        QueryWrapper<UserLoginRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId)
                .last("limit 50");
        return baseMapper.selectList(wrapper);
    }
}
