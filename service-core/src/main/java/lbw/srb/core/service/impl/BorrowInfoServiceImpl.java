package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.BorrowInfoMapper;
import lbw.srb.core.pojo.entity.BorrowInfo;
import lbw.srb.core.service.BorrowInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

}
