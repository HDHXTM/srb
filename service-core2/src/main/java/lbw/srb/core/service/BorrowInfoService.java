package lbw.srb.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.BorrowInfo;
import lbw.srb.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务类
 * </p>

 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    Integer getBorrowInfoStatus(Long userId);

    BigDecimal getBorrowAmount(Long userId);

    List<BorrowInfo> findAll();

    Map<String,Object> show(Long id);

    void approval(BorrowInfoApprovalVO borrowInfoApprovalVO, Long userId);
}
