package lbw.srb.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import lbw.srb.core.pojo.entity.UserAccount;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 */
public interface UserAccountService extends IService<UserAccount> {


    String commitCharge(BigDecimal chargeAmt, Long userId);

    String CQnotify(Map<String, Object> paramMap);

    String withdraw(BigDecimal fetchAmt, Long userId);
    
    BigDecimal getAmount(Long userId);

    String QQnotify(Map<String, Object> paramMap);


    void freeze(Long id, BigDecimal voteAmt);
}
