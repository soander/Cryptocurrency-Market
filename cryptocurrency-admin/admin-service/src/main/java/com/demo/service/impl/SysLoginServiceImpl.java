package com.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.demo.domain.SysMenu;
import com.demo.feign.JwtToken;
import com.demo.feign.OAuth2FeignClient;
import com.demo.model.LoginResult;
import com.demo.service.SysLoginService;
import com.demo.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysLoginServiceImpl implements SysLoginService {
    @Autowired
    private OAuth2FeignClient oAuth2FeignClient;

    @Autowired
    private SysMenuService sysMenuService ;

    @Value("${basic.token:Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=}")
    private String basicToken ;

    @Autowired
    private StringRedisTemplate redisTemplate ;

    /**
     * login
     *
     * @param username
     * @param password
     * @return login result
     */
    @Override
    public LoginResult login(String username, String password) {
        log.info("User{}Login", username);
        // 1 Use authorization-server get token
        ResponseEntity<JwtToken> tokenResponseEntity = oAuth2FeignClient.getToken("password", username, password, "admin_type", basicToken);
        if(tokenResponseEntity.getStatusCode()!= HttpStatus.OK){
            throw new ApiException(ApiErrorCode.FAILED);
        }
        JwtToken jwtToken = tokenResponseEntity.getBody();
        log.info("Success,the token is {}", JSON.toJSONString(jwtToken,true));
        String token = jwtToken.getAccessToken() ;

        // 2 search menu
        Jwt jwt = JwtHelper.decode(token);
        String jwtJsonStr = jwt.getClaims();
        JSONObject jwtJson = JSON.parseObject(jwtJsonStr);
        Long userId = Long.valueOf(jwtJson.getString("user_name"));
        List<SysMenu> menus = sysMenuService.getMenusByUserId(userId);

        // 3. Authorization information
        JSONArray authoritiesJsonArray = jwtJson.getJSONArray("authorities");
        List<SimpleGrantedAuthority> authorities = authoritiesJsonArray.stream()
                                                        .map(authorityJson->new SimpleGrantedAuthority(authorityJson.toString()))
                                                        .collect(Collectors.toList());
        redisTemplate.opsForValue().set(token,"", jwtToken.getExpiresIn(), TimeUnit.SECONDS);
        return new LoginResult(jwtToken.getTokenType() + " " + token, menus, authorities);
    }
}
