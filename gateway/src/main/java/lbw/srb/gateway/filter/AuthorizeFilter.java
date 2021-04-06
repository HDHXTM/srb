//package lbw.srb.gateway.filter;
//
//import lbw.srb.common.util.JwtUtils;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
////自定义全局过滤器，主要用作日志，鉴权
//@Component
//public class AuthorizeFilter implements GlobalFilter, Ordered {
//    private static final String AUTHORIZE_TOKEN = "token";
//
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        //1. 获取请求
//        ServerHttpRequest request = exchange.getRequest();
//        //2. 则获取响应
//        ServerHttpResponse response = exchange.getResponse();
//        //3. 如果是登录,注册请求则放行
//        if (request.getURI().getPath().contains("/api/core/userInfo/login")||
//                request.getURI().getPath().contains("/api/core/userInfo/register")
//                || request.getURI().getPath().contains("/api/sms/send")
//                ||request.getURI().getPath().contains("/admin/")
//        ) {
//            return chain.filter(exchange);
//        }
//        //4. 获取请求头
//        HttpHeaders headers = request.getHeaders();
//        //5. 请求头中获取令牌
//        String token = headers.getFirst(AUTHORIZE_TOKEN);
//
////        验证token
//        if(JwtUtils.checkToken(token))
////没问题，放行
//            return chain.filter(exchange);
//        else {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//    }
//
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
