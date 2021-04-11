package lbw.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.LendItemReturnMapper;
import lbw.srb.core.pojo.entity.LendItemReturn;
import lbw.srb.core.service.LendItemReturnService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {
    @Override
    public List<LendItemReturn> findMyLendItemReturn(Long lendId, Long userId) {
        QueryWrapper<LendItemReturn> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_id",lendId).eq("invest_user_id",userId);
        return baseMapper.selectList(wrapper);
    }
}
