package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.UserAccountMapper;
import lbw.srb.core.pojo.entity.UserAccount;
import lbw.srb.core.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 */
@Service
@Slf4j
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
