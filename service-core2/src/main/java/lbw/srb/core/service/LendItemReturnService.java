package lbw.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.pojo.entity.LendItemReturn;
import lbw.srb.core.pojo.entity.LendReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    List<LendItemReturn> findMyLendItemReturn(Long lendId, Long userId);


    void repaymentPlan(Lend lend, ArrayList<LendReturn> lendReturns);

    List<Map<String, Object>> addReturnDetail(Long lendReturnId);
}
