package lbw.srb.core.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lbw.srb.common.result.R;
import lbw.srb.core.service.BorrowInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "借款管理")
@RestController
@RequestMapping("/admin/core/borrowInfo")
@Slf4j
public class AdminBorrowInfoController {

    @Autowired
    private BorrowInfoService borrowInfoService;

    @ApiOperation("借款信息列表")
    @GetMapping("/list")
    public R list() {
        return R.ok().data("list",borrowInfoService.findAll());
    }

//    @ApiOperation("借款信息详情")
//    @GetMapping("/show/{id}")
//    public R show(
//            @ApiParam(value = "借款信息id", required = true)
//            @PathVariable Long id){
//
//    }
//
//    @ApiOperation("审批借款信息")
//    @PostMapping("/approval")
//    public R approval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO) {
//
//    }
}