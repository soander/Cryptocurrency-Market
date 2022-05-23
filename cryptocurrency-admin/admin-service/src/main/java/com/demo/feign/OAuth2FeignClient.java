package com.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName OAuth2FeignClient
 * @Description The oauth to feign client
 * @Author Yaozheng Wang
 * @Date 2022/5/20 13:55
 **/
@FeignClient(value = "authorization-server")
public interface OAuth2FeignClient {
    @PostMapping("/oauth/token")
    ResponseEntity<JwtToken> getToken(
        @RequestParam("grant_type") String grantType, // The grant type
        @RequestParam("username") String username, // User name
        @RequestParam("password") String  password, // User password
        @RequestParam("login_type")  String loginType, // Login type
        @RequestHeader("Authorization") String basicToken // Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=
    );
}
