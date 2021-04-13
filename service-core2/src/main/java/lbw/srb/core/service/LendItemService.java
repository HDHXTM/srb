package lbw.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.LendItem;
import lbw.srb.core.pojo.vo.InvestVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 */
public interface LendItemService extends IService<LendItem> {

    List<LendItem> findAllByLendId(Long lendId);

    String invest(InvestVO investVO, Long userId);

    String TZnotify(Map<String, Object> check);

    LendItem findByLendNo(String lendNO);

    void setLendItemStartDateByLendId(Long id, LocalDate start, LocalDate end);

}
