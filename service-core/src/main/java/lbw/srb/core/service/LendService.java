package lbw.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.BorrowInfo;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 */
public interface LendService extends IService<Lend> {

    void addLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo, Long userId);

    List<Lend> findAll();

    Map<String, Object> getLendDetail(Long id);

    void makeLoan(Long id);


    BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalmonth, Integer returnMethod);
}
