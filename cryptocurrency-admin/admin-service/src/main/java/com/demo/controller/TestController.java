package com.demo.controller;

import com.demo.domain.SysUser;
import com.demo.model.R;
import com.demo.service.SysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author Yaozheng Wang
 * @Date 2022/5/19 19:53
 **/
@RestController
@Api(tags = "admin-service-test接口")
public class TestController {

    @Autowired
    private SysUserService sysUserService ;

    @GetMapping("/user/info/{id}")
    @ApiOperation(value = "使用用户的id查询用户",authorizations = {@Authorization("Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户的id值")
    })
    public R<SysUser> sysUser(@PathVariable("id")Long id){
        SysUser sysUser = sysUserService.getById(id);
        return R.ok(sysUser) ;
    }
}
