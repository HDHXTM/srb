package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.enums.LendStatusEnum;
import lbw.srb.core.enums.ReturnMethodEnum;
import lbw.srb.core.mapper.BorrowerMapper;
import lbw.srb.core.mapper.LendMapper;
import lbw.srb.core.pojo.entity.BorrowInfo;
import lbw.srb.core.pojo.entity.Borrower;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.pojo.vo.BorrowInfoApprovalVO;
import lbw.srb.core.service.BorrowerService;
import lbw.srb.core.service.DictService;
import lbw.srb.core.service.LendService;
import lbw.srb.core.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 */
@Service
@Slf4j
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Autowired
    private DictService dictService;
    @Autowired
    private BorrowerMapper borrowerMapper;
    @Override
    public List<Lend> findAll() {
        List<Lend> lends = baseMapper.selectList(null);
        for (Lend lend : lends) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("returnMethod",dictService.findNameByCodeAndValue("returnMethod",lend.getReturnMethod()));
            map.put("status",LendStatusEnum.getMsgByStatus(lend.getStatus()));
            lend.setParam(map);
        }
        return lends;
    }


    @Override
    public BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalmonth, Integer returnMethod) {

        BigDecimal interestCount;
        if(returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()){
            interestCount = Amount1Helper.getInterestCount(invest, yearRate, totalmonth);
        }else if(returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()){
            interestCount = Amount2Helper.getInterestCount(invest, yearRate, totalmonth);
        }else if(returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()){
            interestCount = Amount3Helper.getInterestCount(invest, yearRate, totalmonth);
        }else{
            interestCount = Amount4Helper.getInterestCount(invest, yearRate, totalmonth);
        }
        return interestCount;
    }

    @Override
    public void makeLoan(Long id) {
        Lend lend = baseMapper.selectById(id);

    }

    @Override
    public Map<String, Object> getLendDetail(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        Lend lend = baseMapper.selectById(id);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("returnMethod",dictService.findNameByCodeAndValue("returnMethod",lend.getReturnMethod()));
        map2.put("status",LendStatusEnum.getMsgByStatus(lend.getStatus()));
        lend.setParam(map2);
        map.put("lend",lend);

        QueryWrapper<Borrower> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",lend.getUserId());
        map.put("borrower",borrowerMapper.selectOne(wrapper));
        return map;

    }

    @Override
    @Transactional
    public void addLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo, Long userId) {
        Lend lend = new Lend();
        BeanUtils.copyProperties(borrowInfo,lend);
        lend.setId(null);
        lend.setCreateTime(null);
        lend.setUpdateTime(null);
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());

        lend.setBorrowInfoId(borrowInfo.getId());
//        不是有id吗，这啥玩意
        lend.setLendNo(LendNoUtils.getLendNo());
        lend.setLowestAmount(new BigDecimal(100)); //最低投资金额
        lend.setInvestAmount(new BigDecimal(0)); //已投金额
        lend.setInvestNum(0); //已投人数
        //平台预期收益 = 标的金额 * (平台服务费率 / 12 * 期数)
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate.multiply(new BigDecimal(lend.getPeriod())));
        lend.setExpectAmount(expectAmount);

        lend.setCheckAdminId(userId);

        baseMapper.insert(lend);
    }
}
