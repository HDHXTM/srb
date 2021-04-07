package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.mapper.BorrowerMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.pojo.entity.Borrower;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public Integer getBorrowerStatus(Long userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("borrow_auth_status").eq("id",userId);
        List<Object> list = userInfoMapper.selectObjs(wrapper);
        return (Integer)list.get(0);
    }
}
