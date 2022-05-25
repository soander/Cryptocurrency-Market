package com.demo.controller;


import com.demo.geetest.GeetestLib;
import com.demo.geetest.GeetestLibResult;
import com.demo.model.R;
import com.demo.util.IpUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/gt")
public class GeetestController {

    @Autowired
    private GeetestLib geetestLib;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/register")
    @ApiOperation(value = "Get the data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid" ,value = "User's uuid")
    })
    public R<String> register(String uuid) {
        String digestmod = "md5";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("digestmod", digestmod);
        paramMap.put("user_id", uuid);
        paramMap.put("client_type", "web");
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        paramMap.put("ip_address", IpUtil.getIpAddr(servletRequestAttributes.getRequest()));

        GeetestLibResult result = geetestLib.register(digestmod, paramMap);

        redisTemplate.opsForValue().set(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY, result.getStatus(), 180, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(GeetestLib.GEETEST_SERVER_USER_KEY + ":" + uuid, uuid, 180, TimeUnit.SECONDS);
        return R.ok(result.getData());
    }
}
