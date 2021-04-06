package lbw.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.DictMapper;
import lbw.srb.core.pojo.entity.Dict;
import lbw.srb.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 */
@Slf4j
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    @Cacheable(cacheNames = "dict",key = "#parentId")
    public List<Dict> listByParentId(Long parentId) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.select("id","parent_id","name","value","dict_code");
        wrapper.eq("parent_id",parentId);
        List<Dict> list = baseMapper.selectList(wrapper);
        for (Dict dict : list) {
            if (dict.getValue()==null)
                dict.setHasChildren(true);
        }
        return list;
    }

    @Override
    @Transactional
    public boolean saveBatch(Collection<Dict> entityList) {
        this.saveBatch(entityList,entityList.size());
        for (Dict dict : entityList) {
            if (dict.getParentId()!=null)
//                清除缓存，等待下次更新
                redisTemplate.delete("dict::"+dict.getParentId());
        }
        return true;
    }
}
