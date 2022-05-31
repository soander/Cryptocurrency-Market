package com.demo.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(IdProperties.class)
public class IdAutoConfiguration {

    private static IdProperties idProperties;

    private static RestTemplate restTemplate = new RestTemplate();

    public IdAutoConfiguration(IdProperties idProperties) {
        IdAutoConfiguration.idProperties = idProperties;
    }

    public static boolean check(String realName, String cardNum) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","APPCODE " + idProperties.getAppCode());
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                String.format(idProperties.getUrl(), cardNum, realName), HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders), String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            String body = responseEntity.getBody();
            JSONObject jsonObject = JSON.parseObject(body);
            String status = jsonObject.getString("status");
            if("01".equals(status)) {
                return true;
            }
        }
        return  false;
    }
}
