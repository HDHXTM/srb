package lbw.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.pojo.entity.LendReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务类
 * </p>
 */
public interface LendReturnService extends IService<LendReturn> {

    List<LendReturn> findAllByLendId(Long lendId);

    ArrayList<LendReturn> repaymentPlan(Lend lend);

    String commitReturn(Long lendReturnId, Long userId);

    String HQnotify(Map<String, Object> map);
}
