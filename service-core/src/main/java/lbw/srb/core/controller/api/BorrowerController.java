package lbw.srb.core.controller.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lbw.srb.core.controller.BaseController;
import lbw.srb.common.result.R;
import lbw.srb.core.pojo.vo.BorrowerVO;
import lbw.srb.core.service.BorrowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 */
@Api(tags = "借款人")
@RestController
@RequestMapping("/api/core/borrower")
@Slf4j
public class BorrowerController extends BaseController {


    @Resource
    private BorrowerService borrowerService;

    @ApiOperation("保存借款人信息")
    @PostMapping("/auth/save")
    public R save(@Valid @RequestBody BorrowerVO borrowerVO, HttpServletRequest request) {
        borrowerService.saveInfo(borrowerVO,getUserId());
        return R.ok();
    }

    @ApiOperation("获取借款人认证状态")
    @GetMapping("/auth/getBorrowerStatus")
    public R getBorrowerStatus(){
        return R.ok().data("borrowerStatus",borrowerService.getBorrowerStatus(getUserId()));
    }

}

