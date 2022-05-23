package com.demo.controller;

import com.demo.model.LoginResult;
import com.demo.service.SysLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SysLoginController
 * @Description Login controller
 * @Author Yaozheng Wang
 * @Date 2022/5/20 13:45
 **/
@RestController
@Api(tags = "Login controller")
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @PostMapping("/login")
    @ApiOperation(value = "Admin user login")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "username", value = "User Name"),
                    @ApiImplicitParam(name = "password", value = "User Password"),
            }
    )
    public LoginResult login(
            @RequestParam(required = true) String username, // username
            @RequestParam(required = true) String password  // password
    ) {
        return loginService.login(username, password);
    }

}
