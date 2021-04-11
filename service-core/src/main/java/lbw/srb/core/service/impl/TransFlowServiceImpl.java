package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.TransFlowMapper;
import lbw.srb.core.pojo.entity.TransFlow;
import lbw.srb.core.service.TransFlowService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {

    @Override
    public List<TransFlow> findAllByUserId(Long userId) {
        QueryWrapper<TransFlow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean isExist(String agentBillNo) {
        QueryWrapper<TransFlow> wrapper = new QueryWrapper<>();
        wrapper.select("id").eq("trans_no",agentBillNo);
        List<Object> list = baseMapper.selectObjs(wrapper);
        if(list.size()!=0)
            return true;
        return false;
    }
}
