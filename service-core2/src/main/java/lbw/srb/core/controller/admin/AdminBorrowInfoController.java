package lbw.srb.core.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lbw.srb.core.controller.BaseController;
import lbw.srb.common.result.R;
import lbw.srb.core.pojo.vo.BorrowInfoApprovalVO;
import lbw.srb.core.service.BorrowInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "借款管理")
@RestController
@RequestMapping("/admin/core/borrowInfo")
@Slf4j
public class AdminBorrowInfoController extends BaseController {

    @Autowired
    private BorrowInfoService borrowInfoService;

    @ApiOperation("借款信息列表")
    @GetMapping("/list")
    public R list() {
        return R.ok().data("list",borrowInfoService.findAll());
    }

    @ApiOperation("借款信息详情")
    @GetMapping("/show/{id}")
    public R show(
            @ApiParam(value = "借款信息id", required = true)
            @PathVariable Long id){
        return R.ok().data("borrowInfoDetail",borrowInfoService.show(id));
    }
//
    @ApiOperation("审批借款信息")
    @PostMapping("/approval")
    public R approval(@Valid @RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO) {
        borrowInfoService.approval(borrowInfoApprovalVO,getUserId());
        return R.ok().message("成功");
    }
}