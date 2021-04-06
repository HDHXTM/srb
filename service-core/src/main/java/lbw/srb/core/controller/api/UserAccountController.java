//package lbw.srb.core.controller.api;
//
//
//import com.alibaba.fastjson.JSON;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lbw.srb.common.result.R;
//import lbw.srb.core.hfb.RequestHelper;
//import lbw.srb.core.service.UserAccountService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.math.BigDecimal;
//import java.util.Map;
//
///**
// * <p>
// * 用户账户 前端控制器
// * </p>
// */
//@Api(tags = "会员账户")
//@RestController
//@RequestMapping("/api/core/userAccount")
//@Slf4j
//public class UserAccountController {
//
//    @Resource
//    private UserAccountService userAccountService;
//
//    @ApiOperation("充值")
//    @PostMapping("/auth/commitCharge/{chargeAmt}")
//    public R commitCharge(
//            @ApiParam(value = "充值金额", required = true)
//            @PathVariable BigDecimal chargeAmt, HttpServletRequest request) {
//
//    }
//
//    @ApiOperation(value = "用户充值异步回调")
//    @PostMapping("/notify")
//    public String notify(HttpServletRequest request) {
//
//    }
//
//    @ApiOperation("查询账户余额")
//    @GetMapping("/auth/getAccount")
//    public R getAccount(HttpServletRequest request){
//
//    }
//
//    @ApiOperation("用户提现")
//    @PostMapping("/auth/commitWithdraw/{fetchAmt}")
//    public R commitWithdraw(
//            @ApiParam(value = "金额", required = true)
//            @PathVariable BigDecimal fetchAmt, HttpServletRequest request) {
//
//    }
//
//    @ApiOperation("用户提现异步回调")
//    @PostMapping("/notifyWithdraw")
//    public String notifyWithdraw(HttpServletRequest request) {
//
//    }
//
//}
//
