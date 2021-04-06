//package lbw.srb.core.controller.api;
//
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lbw.srb.common.result.R;
//import lbw.srb.core.pojo.entity.BorrowInfo;
//import lbw.srb.core.service.BorrowInfoService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.math.BigDecimal;
//
///**
// * <p>
// * 借款信息表 前端控制器
// * </p>
// */
//@Api(tags = "借款信息")
//@RestController
//@RequestMapping("/api/core/borrowInfo")
//@Slf4j
//public class BorrowInfoController {
//
//    @Resource
//    private BorrowInfoService borrowInfoService;
//
//    @ApiOperation("获取借款额度")
//    @GetMapping("/auth/getBorrowAmount")
//    public R getBorrowAmount(HttpServletRequest request) {
//
//    }
//
//    @ApiOperation("提交借款申请")
//    @PostMapping("/auth/save")
//    public R save(@RequestBody BorrowInfo borrowInfo, HttpServletRequest request) {
//
//    }
//
//    @ApiOperation("获取借款申请审批状态")
//    @GetMapping("/auth/getBorrowInfoStatus")
//    public R getBorrowerStatus(HttpServletRequest request){
//
//    }
//}
