package lbw.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lbw.srb.core.controller.BaseController;
import lbw.srb.common.result.R;
import lbw.srb.core.hfb.RequestHelper;
import lbw.srb.core.pojo.vo.UserBindVO;
import lbw.srb.core.service.UserBindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 */
@Api(tags = "会员账号绑定")
@RestController
@RequestMapping("/api/core/userBind")
@Slf4j
public class UserBindController extends BaseController {

    @Resource
    private UserBindService userBindService;

    @ApiOperation("账户绑定提交数据")
    @PostMapping("/auth/bind")
    public R bind(@Valid @RequestBody UserBindVO userBindVO){
        String formStr=userBindService.bind(userBindVO,getUserId());
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notifyBD(){
        Map<String, Object> map = null;
        try {
            map = check();
        } catch (Exception e) {
            return "fail";
        }
        log.info("账户绑定异步回调接收的参数如下：" + JSON.toJSONString(map));
        return userBindService.notify(map);
    }
}

