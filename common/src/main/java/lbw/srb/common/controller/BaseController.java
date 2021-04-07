package lbw.srb.common.controller;

import lbw.srb.common.result.R;
import lbw.srb.common.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {
    @Autowired
    private HttpServletRequest request;
    public Long getUserId() {
        String token = request.getHeader("token");
        return JwtUtils.getUserId(token);
    }
}
