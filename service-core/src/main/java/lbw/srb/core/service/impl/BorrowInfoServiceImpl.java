package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import lbw.srb.core.pojo.vo.BorrowInfoApprovalVO;
import lbw.srb.core.pojo.vo.BorrowerDetailVO;
import lbw.srb.core.service.BorrowInfoService;
import lbw.srb.core.service.BorrowerService;
import lbw.srb.core.service.DictService;
import lbw.srb.core.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @Autowired
    private BorrowerMapper borrowerMapper;
    @Autowired
    private BorrowerService borrowerService;
    @Autowired
    private LendService lendService;

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
    @Transactional
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO, Long userId) {
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoApprovalVO.getId());
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus());
        borrowInfo.setBorrowYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        baseMapper.updateById(borrowInfo);

//        通过则产生标的
        if (Objects.equals(borrowInfoApprovalVO.getStatus(), BorrowInfoStatusEnum.CHECK_OK.getStatus()))
            lendService.addLend(borrowInfoApprovalVO,borrowInfo,userId);
    }

    @Override
    public Map<String,Object> show(Long id) {
        BorrowInfo borrowInfo = baseMapper.selectById(id);
        UserInfo userInfo = userInfoMapper.selectById(borrowInfo.getUserId());
        borrowInfo.setName(userInfo.getName());
        borrowInfo.setMobile(userInfo.getMobile());
        HashMap<String, Object> map = new HashMap<>();
        map.put("returnMethod", dictService.findNameByCodeAndValue("returnMethod", borrowInfo.getReturnMethod()));
        map.put("moneyUse",dictService.findNameByCodeAndValue("moneyUse",borrowInfo.getMoneyUse()));
        map.put("status",BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus()));
        borrowInfo.setParam(map);

        //查询借款人对象：Borrower(BorrowerDetailVO)
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id", borrowInfo.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);
        BorrowerDetailVO borrowerDetailVO = borrowerService.detail(borrower.getId());

        //组装集合结果
        Map<String, Object> result = new HashMap<>();
        result.put("borrowInfo", borrowInfo);
        result.put("borrower", borrowerDetailVO);
        return result;

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
//                不能查已完成的
                .last("and status !="+ BorrowInfoStatusEnum.FINISH.getStatus());
        List<Object> list = baseMapper.selectObjs(wrapper);
        if (list==null||list.size()==0)
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        return (Integer)list.get(0);
    }
}
