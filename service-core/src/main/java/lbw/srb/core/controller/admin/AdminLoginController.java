package lbw.srb.core.controller.admin;

import lbw.srb.common.result.R;
import lbw.srb.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AdminLoginController {
    @Autowired
    private UserInfoService userInfoService;
    @PostMapping("/admin/core/login")
    public R login(@RequestBody Map<String,String> map, HttpServletRequest request){
        String username = map.get("username");
        String password = map.get("password");

        String token=userInfoService.adminLogin(username,password,request.getRemoteAddr());
        return R.ok().data("token",token);
    }
}
