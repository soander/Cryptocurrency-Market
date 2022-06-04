package com.demo.feign;

import com.demo.config.feign.OAuth2FeignConfig;
import com.demo.dto.AdminBankDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
* @Author Yaozheng Wang
* @Description The admin bank service feign
* @Date 2022/6/2 13:20
* @Param
* @Return * @return null
**/
@FeignClient(name = "admin-service", path = "/adminBanks", configuration = OAuth2FeignConfig.class)
public interface AdminBankServiceFeign {

    @GetMapping("/list")
    List<AdminBankDto> getAllAdminBanks();
}
