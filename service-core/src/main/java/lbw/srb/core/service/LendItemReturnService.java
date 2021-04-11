package lbw.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.LendItemReturn;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    List<LendItemReturn> findMyLendItemReturn(Long lendId, Long userId);
}
