package lbw.srb.gateway.filter;

import lbw.srb.common.exception.BusinessException;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.common.util.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//自定义全局过滤器，用作鉴权
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求
        ServerHttpRequest request = exchange.getRequest();
        // 不需要权限的直接放行
        if (
                !(
                        request.getURI().getPath().contains("/auth/")
                        ||request.getURI().getPath().contains("/admin/")
                )
                || request.getURI().getPath().contains("/login")
            ) {
            return chain.filter(exchange);
        }
        // 获取请求头
        HttpHeaders headers = request.getHeaders();
        // 请求头中获取令牌
        String token = headers.getFirst("token");

        try {
//        验证token,取id
            Long userId = JwtUtils.getUserId(token);
//            System.out.println(userId);
            if (request.getURI().getPath().contains("admin")&&JwtUtils.getUserType(token) != 0) {
//                System.out.println(JwtUtils.getUserType(token));
    //            不是管理员,禁止访问
                    throw new BusinessException();
            }
//        加一个header,其他微服务直接拿id,不用再解析，该值会覆盖已有的同名header,安全！
            ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("Authorization-UserId", String.valueOf(userId)).build();
            ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
            return chain.filter(mutableExchange);
        } catch (BusinessException e) {
            e.printStackTrace();
            ServerHttpResponse response = exchange.getResponse();
            //7. 响应中放入返回的状态吗, 没有权限访问
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //8. 返回
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
