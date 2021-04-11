package lbw.srb.core.controller.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lbw.srb.core.controller.BaseController;
import lbw.srb.common.result.R;
import lbw.srb.core.enums.BorrowInfoStatusEnum;
import lbw.srb.core.pojo.entity.BorrowInfo;
import lbw.srb.core.service.BorrowInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;


/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 */
@Api(tags = "借款信息")
@RestController
@RequestMapping("/api/core/borrowInfo")
@Slf4j
public class BorrowInfoController extends BaseController {

    @Resource
    private BorrowInfoService borrowInfoService;

    @ApiOperation("获取借款额度")
    @GetMapping("/auth/getBorrowAmount")
    public R getBorrowAmount() {
        return R.ok().data("borrowAmount",borrowInfoService.getBorrowAmount(getUserId()));
    }
//
    @ApiOperation("提交借款申请")
    @PostMapping("/auth/save")
    public R save(@Valid @RequestBody BorrowInfo borrowInfo) {
        Long userId = getUserId();
        if (borrowInfo.getAmount().compareTo(borrowInfoService.getBorrowAmount(userId))==1)
            return R.error().message("想屁吃");
        borrowInfo.setUserId(userId);
        borrowInfo.getBorrowYearRate().divide(new BigDecimal(100));
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        borrowInfoService.save(borrowInfo);
        return R.ok();
    }

    @ApiOperation("获取借款申请审批状态")
    @GetMapping("/auth/getBorrowInfoStatus")
    public R getBorrowerStatus(){
        return R.ok().data("borrowInfoStatus",borrowInfoService.getBorrowInfoStatus(getUserId()));
    }
}
