package lbw.srb.core.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lbw.srb.common.result.R;
import lbw.srb.core.pojo.entity.UserInfo;
import lbw.srb.core.pojo.vo.LoginVO;
import lbw.srb.core.pojo.vo.RegisterVO;
import lbw.srb.core.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 */
@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/core/userInfo")
@Slf4j
//@CrossOrigin
public class UserInfoController {

    @Resource
//    private RedisTemplate<String, String> redisTemplate;
    private RedisTemplate redisTemplate;

    @Resource
    private UserInfoService userInfoService;

//    @ApiOperation("会员注册")
//    @PostMapping("/register")
//    public R register(@RequestBody RegisterVO registerVO){
//
//    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request){
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",loginVO.getMobile());
        return R.ok().data("userInfo",userInfoService.getOne(wrapper));
    }

//    @ApiOperation("校验令牌")
//    @GetMapping("/checkToken")
//    public R checkToken(HttpServletRequest request) {
//
//    }
//
//    @ApiOperation("校验手机号是否注册")
//    @GetMapping("/checkMobile/{mobile}")
//    public boolean checkMobile(@PathVariable String mobile){
//
//    }
//
//    @ApiOperation("获取个人空间用户信息")
//    @GetMapping("/auth/getIndexUserInfo")
//    public R getIndexUserInfo(HttpServletRequest request) {
//
//    }
}

