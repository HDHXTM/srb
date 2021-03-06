package lbw.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.Borrower;
import lbw.srb.core.pojo.vo.BorrowerApprovalVO;
import lbw.srb.core.pojo.vo.BorrowerDetailVO;
import lbw.srb.core.pojo.vo.BorrowerVO;

/**
 * <p>
 * 借款人 服务类
 * </p>
 */
public interface BorrowerService extends IService<Borrower> {

    Integer getBorrowerStatus(Long userId);

    void saveInfo(BorrowerVO borrowerVO, Long userId);

    IPage<Borrower> listPage(String keyword, Page<Borrower> borrowerPage);

    BorrowerDetailVO detail(Long id);

    BorrowerDetailVO getDetailByUserId(Long userId);

    String approval(BorrowerApprovalVO borrowerApprovalVO);


}
