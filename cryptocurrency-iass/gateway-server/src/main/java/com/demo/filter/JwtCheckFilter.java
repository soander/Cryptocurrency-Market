package com.demo.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;

/**
 * @ClassName JwtCheckFilter
 * @Description The jwt check redis stored token
 * @Author Yaozheng Wang
 * @Date 2022/5/18 14:25
 **/
@Component
public class JwtCheckFilter implements GlobalFilter, Ordered {

    @Value("${no.token.access.urls:/admin/login,/admin/validate/code}")
    private Set<String> noTokenAccessUrls;

    @Override
    public int getOrder() {
        return 0;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Filter about the token
    * @Date 2022/5/18 14:26
    * @Param
    * @Return * @return null
    **/
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // No need token
        if (allowNoTokenAccess(exchange)) {
            return chain.filter(exchange);
        }

        // Get token
        String token = getToken(exchange);
        if (StringUtils.isEmpty(token)) { // token is empty
            return buildUNAuthorizedResult(exchange);
        }
        if (false) {
            return buildUNAuthorizedResult(exchange);
        }
        return chain.filter(exchange);
    }

    private boolean allowNoTokenAccess(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        if (noTokenAccessUrls.contains(path)) {
            return true;
        }
        return false;
    }

    private String getToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorization) || authorization.trim().isEmpty()) {
            return null;
        }
        return authorization.replace("bearer ", "");
    }

    private Mono<Void> buildUNAuthorizedResult(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", "unauthorized");
        jsonObject.put("error_description", "invalid_token");
        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes());
        return response.writeWith(Flux.just(dataBuffer));
    }
}
