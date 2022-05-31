package com.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.demo.feign.JwtToken;
import com.demo.feign.OAuth2FeignClient;
import com.demo.geetest.GeetestLib;
import com.demo.geetest.GeetestLibResult;
import com.demo.model.LoginForm;
import com.demo.model.LoginUser;
import com.demo.service.LoginService;
import com.demo.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private OAuth2FeignClient oAuth2FeignClient;

    @Value("${basic.token:Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=}")
    private String basicToken;

    @Autowired
    private StringRedisTemplate strRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GeetestLib geetestLib;

    // Member user login
    @Override
    public LoginUser login(LoginForm loginForm) {
        log.info("User{}begin login", loginForm.getUsername());
        checkFormData(loginForm);
        LoginUser loginUser = null;

        ResponseEntity<JwtToken> tokenResponseEntity = oAuth2FeignClient.getToken("password", loginForm.getUsername(), loginForm.getPassword(), "member_type", basicToken);
        if (tokenResponseEntity.getStatusCode() == HttpStatus.OK) {
            JwtToken jwtToken = tokenResponseEntity.getBody();
            log.info("Feign success, result is ", JSON.toJSONString(jwtToken, true));
            loginUser = new LoginUser(loginForm.getUsername(), jwtToken.getExpiresIn(), jwtToken.getTokenType() + " " + jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            strRedisTemplate.opsForValue().set(jwtToken.getAccessToken(), "", jwtToken.getExpiresIn(), TimeUnit.SECONDS);
        }
        return loginUser;
    }

    private void checkFormData(LoginForm loginForm) {
        String challenge = loginForm.getGeetest_challenge();
        String validate = loginForm.getGeetest_validate();
        String seccode = loginForm.getGeetest_seccode();
        int status;
        String userId;

        String statusStr = redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY).toString();
        status = Integer.valueOf(statusStr).intValue();
        userId = redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_USER_KEY + ":" + loginForm.getUuid()).toString();
        GeetestLibResult result;
        if (status == 1) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("user_id", userId);
            paramMap.put("client_type", "web");
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            paramMap.put("ip_address", IpUtil.getIpAddr(servletRequestAttributes.getRequest()));
            result = geetestLib.successValidate(challenge, validate, seccode, paramMap);
            log.info("Check result is {}", JSON.toJSONString(result));
        } else {
            result = geetestLib.failValidate(challenge, validate, seccode);
        }
        if(result.getStatus() != 1) {
            log.error("Validation exception",JSON.toJSONString(result,true));
            throw new IllegalArgumentException("Validation exception");
        }
    }
}
