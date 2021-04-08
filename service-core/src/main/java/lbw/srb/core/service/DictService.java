package lbw.srb.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.Dict;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 */
public interface DictService extends IService<Dict> {
    List<Dict> listByParentId(Long parentId);

    List<Dict> findByDictCode(String dictCode);

    String findNameByCodeAndValue(String code,Integer value);
}
