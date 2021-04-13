package lbw.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.core.enums.ReturnMethodEnum;
import lbw.srb.core.mapper.LendItemMapper;
import lbw.srb.core.mapper.LendItemReturnMapper;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.pojo.entity.LendItem;
import lbw.srb.core.pojo.entity.LendItemReturn;
import lbw.srb.core.pojo.entity.LendReturn;
import lbw.srb.core.service.*;
import lbw.srb.core.util.Amount1Helper;
import lbw.srb.core.util.Amount2Helper;
import lbw.srb.core.util.Amount3Helper;
import lbw.srb.core.util.Amount4Helper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {
    @Autowired
    private LendItemMapper lendItemMapper;
    @Autowired
    private LendService lendService;
    @Autowired
    private LendReturnService lendReturnService;
    @Autowired
    private UserBindService userBindService;
    @Autowired
    private LendItemService lendItemService;



    @Override
    public List<Map<String, Object>> addReturnDetail(Long lendReturnId) {
        LendReturn lendReturn = lendReturnService.getById(lendReturnId);
        Lend lend = lendService.getById(lendReturn.getLendId());
        QueryWrapper<LendItemReturn> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_return_id",lendReturnId);
        List<LendItemReturn> lendItemReturns = baseMapper.selectList(wrapper);
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        for (LendItemReturn lendItemReturn : lendItemReturns) {
            String bindCode = userBindService.getBindCodeByUserId(lendItemReturn.getInvestUserId());
            QueryWrapper<LendItem> wrapper1 = new QueryWrapper<>();
            wrapper1.select("lend_item_no").eq("id",lendItemReturn.getLendItemId());

            HashMap<String, Object> map = new HashMap<>();
            map.put("agentProjectCode", lend.getLendNo());//项目编号
            map.put("voteBillNo", lendItemMapper.selectObjs(wrapper1).get(0));//投资编号
            map.put("toBindCode", bindCode); //投资人bindCode
            map.put("transitAmt", lendItemReturn.getTotal());//还款总额
            map.put("baseAmt", lendItemReturn.getPrincipal());//本金
            map.put("benifitAmt", lendItemReturn.getInterest());//利息
            map.put("feeAmt", new BigDecimal(0));
            res.add(map);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repaymentPlan(Lend lend,ArrayList<LendReturn> lendReturns) {
        List<LendItem> lendItems = lendItemService.findAllByLendId(lend.getId());
        LendItemReturn base = new LendItemReturn();
        base.setLendId(lend.getId());
        base.setLendYearRate(lend.getLendYearRate());
        base.setOverdue(false);
        base.setReturnMethod(lend.getReturnMethod());
        base.setStatus(0);

//        每次投资，都要有还款计划
        for (LendItem lendItem : lendItems) {
            //根据还款方式计算本金和利息
            Map<Integer, BigDecimal> mapInterest = null;  //还款期数 -> 利息
            Map<Integer, BigDecimal> mapPrincipal = null; //还款期数 -> 本金
            if (lend.getReturnMethod().intValue() == ReturnMethodEnum.ONE.getMethod()) {
                //利息
                mapInterest = Amount1Helper.getPerMonthInterest(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
                //本金
                mapPrincipal = Amount1Helper.getPerMonthPrincipal(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
            } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.TWO.getMethod()) {
                mapInterest = Amount2Helper.getPerMonthInterest(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
                mapPrincipal = Amount2Helper.getPerMonthPrincipal(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
            } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.THREE.getMethod()) {
                mapInterest = Amount3Helper.getPerMonthInterest(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
                mapPrincipal = Amount3Helper.getPerMonthPrincipal(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
            } else {
                mapInterest = Amount4Helper.getPerMonthInterest(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
                mapPrincipal = Amount4Helper.getPerMonthPrincipal(lendItem.getInvestAmount(), lend.getLendYearRate(), lend.getPeriod());
            }
            for (int i = 1; i <=lend.getPeriod() ; i++) {
                LendItemReturn lendItemReturn = new LendItemReturn();
                BeanUtils.copyProperties(base,lendItemReturn);
                lendItemReturn.setLendReturnId(lendReturns.get(i-1).getId());
                lendItemReturn.setLendItemId(lendItem.getId());
                lendItemReturn.setInvestUserId(lendItem.getInvestUserId());
                lendItemReturn.setInvestAmount(lendItem.getInvestAmount());
                lendItemReturn.setCurrentPeriod(i);
                lendItemReturn.setPrincipal(mapPrincipal.get(i));
                lendItemReturn.setInterest(mapInterest.get(i));
                lendItemReturn.setTotal(mapPrincipal.get(i).add(mapInterest.get(i)));
                lendItemReturn.setReturnDate(lend.getLendStartDate().plusMonths(i));
                baseMapper.insert(lendItemReturn);
            }
        }
    }

    @Override
    public List<LendItemReturn> findMyLendItemReturn(Long lendId, Long userId) {
        QueryWrapper<LendItemReturn> wrapper = new QueryWrapper<>();
        wrapper.eq("lend_id",lendId).eq("invest_user_id",userId);
        return baseMapper.selectList(wrapper);
    }

    //每天0点查看是否有逾期单
    @Scheduled(cron = "0 0 0 * * *")
    public void task(){
        QueryWrapper<LendItemReturn> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0)
                .lt("return_date", LocalDate.now());
        List<LendItemReturn> lendItemReturns = baseMapper.selectList(wrapper);
        for (LendItemReturn lendItemReturn : lendItemReturns) {
            lendItemReturn.setOverdue(true);
            lendItemReturn.setOverdueTotal(lendItemReturn.getTotal());
            baseMapper.updateById(lendItemReturn);
            log.warn("逾期！！"+lendItemReturn);
        }
    }
}
