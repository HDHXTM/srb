package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.enums.BorrowInfoStatusEnum;
import lbw.srb.core.enums.ReturnMethodEnum;
import lbw.srb.core.mapper.BorrowInfoMapper;
import lbw.srb.core.mapper.BorrowerMapper;
import lbw.srb.core.mapper.IntegralGradeMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.pojo.entity.BorrowInfo;
import lbw.srb.core.pojo.entity.Borrower;
import lbw.srb.core.pojo.entity.IntegralGrade;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.service.BorrowInfoService;
import lbw.srb.core.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private IntegralGradeMapper integralGradeMapper;
    @Autowired
    private DictService dictService;

    @Override
    public List<BorrowInfo> findAll() {
        List<BorrowInfo> list = baseMapper.selectList(null);
        for (BorrowInfo borrowInfo : list) {
            UserInfo userInfo = userInfoMapper.selectById(borrowInfo.getUserId());
            borrowInfo.setName(userInfo.getName());
            borrowInfo.setMobile(userInfo.getMobile());
            HashMap<String, Object> map = new HashMap<>();
            map.put("returnMethod", dictService.findNameByCodeAndValue("returnMethod", borrowInfo.getReturnMethod()));
            map.put("moneyUse",dictService.findNameByCodeAndValue("moneyUse",borrowInfo.getMoneyUse()));
            map.put("status",BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus()));
            borrowInfo.setParam(map);
        }
        return list;
    }

    @Override
    public BigDecimal getBorrowAmount(Long userId) {
//        取用户积分
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("integral").eq("id",userId);
        List<Object> list = userInfoMapper.selectObjs(wrapper);
        Integer integral= (Integer) list.get(0);

        QueryWrapper<IntegralGrade> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("borrow_amount")
                .gt("integral_end",integral)
                .le("integral_start",integral);
        List<Object> objs = integralGradeMapper.selectObjs(queryWrapper);
        return (BigDecimal)objs.get(0);
    }

    @Override
    public Integer getBorrowInfoStatus(Long userId) {
        QueryWrapper<BorrowInfo> wrapper = new QueryWrapper<>();
        wrapper
                .select("status")
                .eq("user_id",userId)
                .last("and user_id !="+ BorrowInfoStatusEnum.FINISH.getStatus());
        List<Object> list = baseMapper.selectObjs(wrapper);
        if (list==null||list.size()==0)
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        return (Integer)list.get(0);
    }
}
