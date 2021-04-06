package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.LendItemMapper;
import lbw.srb.core.pojo.entity.LendItem;
import lbw.srb.core.service.LendItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 */
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {

}
