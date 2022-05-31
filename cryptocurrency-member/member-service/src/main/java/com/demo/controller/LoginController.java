package com.demo.controller;

import com.demo.model.LoginForm;
import com.demo.model.LoginUser;
import com.demo.model.R;
import com.demo.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Login controller")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @ApiOperation(value = "Member login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginForm",value = "Login form")
    })
    public R<LoginUser> login(@RequestBody @Validated  LoginForm loginForm) {
       LoginUser loginUser = loginService.login(loginForm);
       return R.ok(loginUser);
    }
}
