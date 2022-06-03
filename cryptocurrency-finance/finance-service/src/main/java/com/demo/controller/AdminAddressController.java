package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.AdminAddress;
import com.demo.model.R;
import com.demo.service.AdminAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/adminAddress")
@Api(tags = "Admin address controller")
public class AdminAddressController {

    @Autowired
    private AdminAddressService adminAddressService;

    @GetMapping
    @ApiOperation(value = "Admin address list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "Coin id"),
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size")
    })
    public R<Page<AdminAddress>> findByPage(@ApiIgnore Page<AdminAddress> page, Long coinId) {
        Page<AdminAddress> adminAddressPage = adminAddressService.findByPage(page, coinId);
        return R.ok(adminAddressPage);
    }

    @PostMapping
    @ApiOperation(value = "Add admin address")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminAddress", value = "adminAddress json")
    })
    public R save(@RequestBody @Validated AdminAddress adminAddress) {
        boolean save = adminAddressService.save(adminAddress);
        if (save) {
            return R.ok();
        }
        return R.fail("Add fail");
    }

    @PatchMapping
    @ApiOperation(value = "Update admin address")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminAddress", value = "adminAddress json")
    })
    public R update(@RequestBody @Validated AdminAddress adminAddress) {
        boolean update = adminAddressService.updateById(adminAddress);
        if (update) {
            return R.ok();
        }
        return R.fail("Update fail");
    }
}
