package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.LendReturnMapper;
import lbw.srb.core.pojo.entity.LendReturn;
import lbw.srb.core.service.LendReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 还款记录表 服务实现类
 * </p>
 */
@Service
@Slf4j
public class LendReturnServiceImpl extends ServiceImpl<LendReturnMapper, LendReturn> implements LendReturnService {

    @Override
    public List<LendReturn> findAllByLendId(Long lendId) {
        QueryWrapper<LendReturn> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_id",lendId);
        return baseMapper.selectList(wrapper);
    }
}
