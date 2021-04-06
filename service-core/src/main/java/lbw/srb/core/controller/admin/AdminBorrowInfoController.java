//package lbw.srb.core.controller.admin;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lbw.srb.common.result.R;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//
//@Api(tags = "借款管理")
//@RestController
//@RequestMapping("/admin/core/borrowInfo")
//@Slf4j
//public class AdminBorrowInfoController {
//
//
//    @ApiOperation("借款信息列表")
//    @GetMapping("/list")
//    public R list() {
//
//    }
//
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
//}