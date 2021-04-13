package lbw.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lbw.srb.core.controller.BaseController;
import lbw.srb.common.result.R;
import lbw.srb.core.hfb.RequestHelper;
import lbw.srb.core.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 */
@Api(tags = "会员账户")
@RestController
@RequestMapping("/api/core/userAccount")
@Slf4j
public class UserAccountController extends BaseController {

    @Resource
    private UserAccountService userAccountService;

    @ApiOperation("充值")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public R commitCharge(
            @ApiParam(value = "充值金额", required = true)
            @PathVariable BigDecimal chargeAmt) {
        //组装表单字符串，用于远程提交数据
        String formStr = userAccountService.commitCharge(chargeAmt, getUserId());
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation(value = "用户充值异步回调")
    @PostMapping("/notify")
    public String notifyCQ() {
        Map<String, Object> map = null;
        try {
            map = check();
        } catch (Exception e) {
            return "fail";
        }
        log.info("用户充值异步回调：" + JSON.toJSONString(map));
        return userAccountService.CQnotify(map);
    }

    @ApiOperation("查询账户余额")
    @GetMapping("/auth/getAccount")
    public R getAccount(){
        return R.ok().data("account",userAccountService.getAmount(getUserId()));
    }
//
    @ApiOperation("用户提现")
    @PostMapping("/auth/commitWithdraw/{fetchAmt}")
    public R commitWithdraw(
            @ApiParam(value = "金额", required = true)
            @PathVariable BigDecimal fetchAmt) {
        String formStr=userAccountService.withdraw(fetchAmt,getUserId());
        return R.ok().data("formStr", formStr);
    }
//
    @ApiOperation("用户提现异步回调")
    @PostMapping("/notifyWithdraw")
    public String notifyWithdraw() {
        Map<String, Object> map = null;
        try {
            map = check();
        } catch (Exception e) {
            return "fail";
        }
        log.info("用户取钱异步回调：" + JSON.toJSONString(map));
        return userAccountService.QQnotify(map);
    }

}

