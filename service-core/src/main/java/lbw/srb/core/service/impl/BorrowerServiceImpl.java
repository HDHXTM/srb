package lbw.srb.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lbw.srb.common.util.RedisUtil;
import lbw.srb.core.enums.BorrowerStatusEnum;
import lbw.srb.core.enums.IntegralEnum;
import lbw.srb.core.mapper.BorrowerAttachMapper;
import lbw.srb.core.mapper.BorrowerMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.mapper.UserIntegralMapper;
import lbw.srb.core.pojo.entity.Borrower;
import lbw.srb.core.pojo.entity.BorrowerAttach;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.entity.UserIntegral;
import lbw.srb.core.pojo.vo.BorrowerApprovalVO;
import lbw.srb.core.pojo.vo.BorrowerAttachVO;
import lbw.srb.core.pojo.vo.BorrowerDetailVO;
import lbw.srb.core.pojo.vo.BorrowerVO;
import lbw.srb.core.service.BorrowerAttachService;
import lbw.srb.core.service.BorrowerService;
import lbw.srb.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 */
@Service
@Slf4j
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BorrowerAttachService borrowerAttachService;
    @Autowired
    private BorrowerMapper borrowerMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserIntegralMapper userIntegralMapper;


    @Override
    public String approval(BorrowerApprovalVO borrowerApprovalVO) {
        QueryWrapper<Borrower> wrapper = new QueryWrapper<>();
        wrapper.eq("id",borrowerApprovalVO.getBorrowerId());
        Borrower borrower = borrowerMapper.selectOne(wrapper);
        borrower.setStatus(borrowerApprovalVO.getStatus());
        borrowerMapper.updateById(borrower);
        UserInfo userInfo = userInfoMapper.selectById(borrower.getUserId());
        Integer sum=userInfo.getIntegral();
        if (Objects.equals(borrowerApprovalVO.getStatus(), BorrowerStatusEnum.AUTH_OK.getStatus())) {
            sum += borrowerApprovalVO.getInfoIntegral();
//        更新积分记录
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setUserId(borrower.getUserId());
            userIntegral.setContent(IntegralEnum.BORROWER_INFO.getMsg());
            userIntegral.setIntegral(sum);
            userIntegralMapper.insert(userIntegral);

            //身份证积分
            if (borrowerApprovalVO.getIsIdCardOk()) {
                userIntegral = new UserIntegral();
                userIntegral.setUserId(borrower.getUserId());
                userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
                userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
                userIntegralMapper.insert(userIntegral);
                sum += IntegralEnum.BORROWER_IDCARD.getIntegral();
            }

            //房产积分
            if (borrowerApprovalVO.getIsHouseOk()) {
                userIntegral = new UserIntegral();
                userIntegral.setUserId(borrower.getUserId());
                userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
                userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
                userIntegralMapper.insert(userIntegral);
                sum += IntegralEnum.BORROWER_HOUSE.getIntegral();
            }

            //车辆积分
            if (borrowerApprovalVO.getIsCarOk()) {
                userIntegral = new UserIntegral();
                userIntegral.setUserId(borrower.getUserId());
                userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
                userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
                userIntegralMapper.insert(userIntegral);
                sum += IntegralEnum.BORROWER_CAR.getIntegral();
            }
        }

        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfo.setIntegral(sum+userInfo.getIntegral());
        userInfoMapper.updateById(userInfo);

        return "成功";
    }

    @Override
    public BorrowerDetailVO getDetailByUserId(Long userId) {
        QueryWrapper<Borrower> wrapper = new QueryWrapper<>();
        wrapper.select("id").eq("user_id",userId);
        List<Object> objects = borrowerMapper.selectObjs(wrapper);
        return detail((Long)objects.get(0));
    }

    @Override
    public BorrowerDetailVO detail(Long id) {
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        QueryWrapper<Borrower> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        Borrower borrower = borrowerMapper.selectOne(wrapper);
        BeanUtils.copyProperties(borrower,borrowerDetailVO);
        borrowerDetailVO.setSex(borrower.getSex()==1?"男":"女");
        borrowerDetailVO.setMarry(borrower.getMarry()?"是":"否");
        borrowerDetailVO.setStatus(BorrowerStatusEnum.getMsgByStatus(borrower.getStatus()));
        borrowerDetailVO.setEducation(dictService.findNameByCodeAndValue("education",borrower.getEducation()));
        borrowerDetailVO.setIncome(dictService.findNameByCodeAndValue("income",borrower.getIncome()));
        borrowerDetailVO.setReturnSource(dictService.findNameByCodeAndValue("returnSource",borrower.getReturnSource()));
        borrowerDetailVO.setIndustry(dictService.findNameByCodeAndValue("industry",borrower.getIndustry()));
        borrowerDetailVO.setContactsRelation(dictService.findNameByCodeAndValue("relation",borrower.getContactsRelation()));

        QueryWrapper<BorrowerAttach> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("borrower_id",borrower.getUserId());
        List<BorrowerAttach> ba = borrowerAttachService.list(wrapper1);
        ArrayList<BorrowerAttachVO> bav = new ArrayList<>();
        for (BorrowerAttach borrowerAttach : ba) {
            BorrowerAttachVO borrowerAttachVO = new BorrowerAttachVO();
            BeanUtils.copyProperties(borrowerAttach,borrowerAttachVO);
            bav.add(borrowerAttachVO);
        }
        borrowerDetailVO.setBorrowerAttachVOList(bav);

        return borrowerDetailVO;
    }

    @Override
    public IPage<Borrower> listPage(String keyword, Page<Borrower> borrowerPage) {
        QueryWrapper<Borrower> wrapper = new QueryWrapper<>();
        if (!StringUtils.checkValNotNull(keyword))
            return baseMapper.selectPage(borrowerPage,null);
        wrapper.like("name",keyword)
                .or()
                .like("mobile",keyword)
                .or()
                .like("id_card",keyword);
        return baseMapper.selectPage(borrowerPage,wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInfo(BorrowerVO borrowerVO, Long userId) {
//        借款表
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO,borrower);
        UserInfo userInfo = userInfoMapper.selectById(userId);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(1);
        baseMapper.insert(borrower);

//        附件表
        List<BorrowerAttach> attachList = borrowerVO.getBorrowerAttachList();
        for (BorrowerAttach borrowerAttach : attachList) {
            borrowerAttach.setBorrowerId(userId);
        }
        borrowerAttachService.saveBatch(attachList);

//        更新用户信息
        userInfo.setBorrowAuthStatus(1);
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Integer getBorrowerStatus(Long userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("borrow_auth_status").eq("id",userId);
        List<Object> list = userInfoMapper.selectObjs(wrapper);
        return (Integer)list.get(0);
    }
}
