package com.demo.feign;

import com.demo.config.feign.OAuth2FeignConfig;
import com.demo.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserServiceFeign
 * @Description User service feign
 * @Author Yaozheng Wang
 * @Date 2022/5/29 16:34
 **/
@FeignClient(name = "member-service", configuration = OAuth2FeignConfig.class, path = "/users")
public interface UserServiceFeign {
    // Admin-service uses Member-service to query user list
    @GetMapping("/basic/users")
    Map<Long,UserDto> getBasicUsers(
            @RequestParam(value = "ids",required = false) List<Long> ids,
            @RequestParam(value = "userName",required = false) String userName ,
            @RequestParam(value = "mobile",required = false) String mobile
    ) ;
}