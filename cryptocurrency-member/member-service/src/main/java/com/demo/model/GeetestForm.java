package com.demo.model;

import com.alibaba.fastjson.JSON;
import com.demo.geetest.GeetestLib;
import com.demo.geetest.GeetestLibResult;
//import com.demo.util.IpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class GeetestForm {

    private String geetest_challenge;
    private String geetest_seccode;
    private String geetest_validate;
    private String uuid;

    public void check(GeetestLib geetestLib, RedisTemplate<String,Object> redisTemplate) {
        String challenge = this.getGeetest_challenge();
        String validate = this.getGeetest_validate();
        String seccode = this.getGeetest_seccode();
        int status = 0;
        String userId = "";
        String statusStr = redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY).toString();
        status = Integer.valueOf(statusStr).intValue();
        userId = redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_USER_KEY + ":" + this.getUuid()).toString();
        GeetestLibResult result = null;

        if (status == 1) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("user_id", userId);
            paramMap.put("client_type", "web");
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            paramMap.put("ip_address", IpUtil.getIpAddr(servletRequestAttributes.getRequest()));
//            result = geetestLib.successValidate(challenge, validate, seccode, paramMap);
            log.info("Authorization result is {}", JSON.toJSONString(result));
        } else {
            result = geetestLib.failValidate(challenge, validate, seccode);
        }
        if(result.getStatus()!=1){
            log.error("Authorization error",JSON.toJSONString(result,true));
            throw new IllegalArgumentException("Authorization error");
        }
    }
}
