package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.Config;
import com.demo.model.R;
import com.demo.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/configs")
@Api(tags = "Admin configs")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @GetMapping
    @ApiOperation(value = "Query admin configs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type" ,value = "Config type"),
            @ApiImplicitParam(name = "code" ,value = "Config code"),
            @ApiImplicitParam(name = "name" ,value = "Config name"),
            @ApiImplicitParam(name = "current" ,value = "Current page"),
            @ApiImplicitParam(name = "size" ,value = "Page size"),

    })
    @PreAuthorize("hasAuthority('config_query')")
    public R<Page<Config>> findByPage(@ApiIgnore Page<Config> page, String type, String code, String name) {
        Page<Config> configPage = configService.findByPage(page,type,name,code);
        return R.ok(configPage);
    }

    @PostMapping
    @ApiOperation(value = "Add a config")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "config",value = "config's json information")
    })
    @PreAuthorize("hasAuthority('config_create')")
    public R add(@RequestBody @Validated  Config config) {
        boolean save = configService.save(config);
        if(save) {
            return R.ok();
        }
        return R.fail("Add fail");
    }

    @PatchMapping
    @ApiOperation(value = "Update a config")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "config",value = "config's json information")
    })
    @PreAuthorize("hasAuthority('config_update')")
    public R update(@RequestBody @Validated  Config config) {
        boolean update = configService.updateById(config);
        if(update) {
            return R.ok();
        }
        return R.fail("Update fail");
    }
}
