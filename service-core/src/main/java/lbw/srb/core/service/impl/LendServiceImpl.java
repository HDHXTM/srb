package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.LendMapper;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.service.LendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 */
@Service
@Slf4j
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

}
