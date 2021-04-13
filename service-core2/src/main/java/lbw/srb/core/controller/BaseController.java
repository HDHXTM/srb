package lbw.srb.core.controller;

import com.alibaba.fastjson.JSON;
import lbw.srb.common.exception.BusinessException;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.core.hfb.RequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
public class BaseController {
    @Autowired
    private HttpServletRequest request;
//    public Long getUserId() {
//        String token = request.getHeader("token");
//        return JwtUtils.getUserId(token);
//    }

    public Long getUserId(){
        return Long.parseLong(request.getHeader("Authorization-UserId"));
    }

    public Map<String, Object> check() throws Exception {
        //汇付宝向尚融宝发起回调请求时携带的参数
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        //验签
        if (RequestHelper.isSignEquals(paramMap)) {
            //判断业务是否成功
            if ("0001".equals(paramMap.get("resultCode")))
                //同步账户数据
                return paramMap;
        }
        log.error("回调异常"+ JSON.toJSONString(paramMap));
        throw new Exception();
    }
}
