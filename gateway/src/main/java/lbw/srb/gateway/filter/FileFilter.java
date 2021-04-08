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
////访问文件服务时，要登录
//@Component
//public class FileFilter implements GlobalFilter, Ordered {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        //1. 获取请求
//        ServerHttpRequest request = exchange.getRequest();
//        //2. 则获取响应
//        ServerHttpResponse response = exchange.getResponse();
//        //3. 如果是登录,注册请求则放行
//        if (request.getURI().getPath().contains("/api/oss/file/")) {
//            //4. 获取请求头
//            HttpHeaders headers = request.getHeaders();
//            //5. 请求头中获取令牌
//            String token = headers.getFirst("token");
//            if (JwtUtils.checkToken(token))
//                return chain.filter(exchange);
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
