//package lbw.srb.core.controller.admin;
//
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lbw.srb.common.result.R;
//import lbw.srb.core.service.UserLoginRecordService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * <p>
// * 用户登录记录表 前端控制器
// * </p>
// *
// * @author Helen
// * @since 2021-02-20
// */
//@Api(tags = "会员登录日志接口")
//@RestController
//@RequestMapping("/admin/core/userLoginRecord")
//@Slf4j
//public class AdminUserLoginRecordController {
//
//    @Resource
//    private UserLoginRecordService userLoginRecordService;
//
//    @ApiOperation("获取会员登录日志列表")
//    @GetMapping("/listTop50/{userId}")
//    public R listTop50(
//            @ApiParam(value ="用户id", required = true)
//            @PathVariable Long userId){
//
//    }
//}
//
