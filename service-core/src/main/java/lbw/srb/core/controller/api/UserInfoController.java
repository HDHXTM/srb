package lbw.srb.core.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lbw.srb.common.controller.BaseController;
import lbw.srb.common.exception.Assert;
import lbw.srb.common.result.R;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.common.util.JwtUtils;
import lbw.srb.common.util.RedisUtil;
import lbw.srb.common.util.RegexValidateUtils;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.vo.LoginVO;
import lbw.srb.core.pojo.vo.RegisterVO;
import lbw.srb.core.pojo.vo.UserIndexVO;
import lbw.srb.core.pojo.vo.UserInfoVO;
import lbw.srb.core.service.UserInfoService;
import lbw.srb.rabbitMQ.product.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 */
@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/core/userInfo")
@Slf4j
public class UserInfoController extends BaseController {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SMSService smsService;
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public R register(@Valid @RequestBody RegisterVO registerVO){
        String code = (String) redisUtil.get("srb:sms:code:" + registerVO.getMobile());
        Assert.equals(code,registerVO.getCode(),ResponseEnum.CODE_ERROR);
        userInfoService.register(registerVO);
        return R.ok();
    }
    @ApiOperation("发送验证码")
    @GetMapping("/sendSms/{phone}")
    public void send(@PathVariable("phone") String phone){
        //校验手机号码不能为空
        Assert.notEmpty(phone, ResponseEnum.MOBILE_NULL_ERROR);
        //是否是合法的手机号码
        Assert.isTrue(RegexValidateUtils.checkCellphone(phone), ResponseEnum.MOBILE_ERROR);
//        查看手机号是否注册
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", phone);
        Assert.isNull(userInfoService.getOne(wrapper),ResponseEnum.MOBILE_EXIST_ERROR);
        smsService.sendRegisterMessage(phone);
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request){
        String ip = request.getRemoteAddr();
        return R.ok().data("userInfo",userInfoService.login(loginVO,ip));
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if(JwtUtils.checkToken(token))
            return R.ok();
        return R.error().message("token无效");
    }
//
//
    @ApiOperation("获取个人空间用户信息")
    @GetMapping("/auth/getIndexUserInfo")
    public R getIndexUserInfo(HttpServletRequest request) {
//        String token = request.getHeader("token");
//        Long userId = JwtUtils.getUserId(token);
        return R.ok().data("userIndexVO",userInfoService.getIndexUserInfo(getUserId()));
    }
}

