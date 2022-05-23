package com.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.AdminBank;
//import com.demo.dto.AdminBankDto;
//import com.demo.feign.AdminBankServiceFeign;
import com.demo.model.R;
import com.demo.service.AdminBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/adminBanks")
@Api(tags = "Bank config")
public class AdminBankController {

    @Autowired
    private AdminBankService adminBankService;


    @GetMapping
    @ApiOperation(value = "Query bank")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankCard" ,value = "Bank card"),
            @ApiImplicitParam(name = "current" ,value = "Current page"),
            @ApiImplicitParam(name = "size" ,value = "Page size")
    })
    @PreAuthorize("hasAuthority('admin_bank_query')")
    public R<Page<AdminBank>> findByPage(@ApiIgnore Page<AdminBank> page, String bankCard) {
        Page<AdminBank> adminBankPage = adminBankService.findByPage(page, bankCard);
        return R.ok(adminBankPage);
    }


    @PostMapping
    @ApiOperation(value = "Add a bank")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminBank", value = "adminBank's json information")
    })
    @PreAuthorize("hasAuthority('admin_bank_create')")
    public R add(@RequestBody @Validated AdminBank adminBank) {
        boolean save = adminBankService.save(adminBank);
        if(save) {
            return R.ok();
        }
        return R.fail("Add fail");
    }


    @PatchMapping
    @ApiOperation(value = "Update a bank")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminBank" ,value = "adminBank's json information")
    })
    @PreAuthorize("hasAuthority('admin_bank_update')")
    public R update(@RequestBody @Validated AdminBank adminBank) {
        boolean updateById = adminBankService.updateById(adminBank);
        if(updateById) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @PostMapping("/adminUpdateBankStatus")
    @ApiOperation(value = "Update bank status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId" ,value = "Bank id"),
            @ApiImplicitParam(name = "status" ,value = "Bank status")
    })
    @PreAuthorize("hasAuthority('admin_bank_update')")
    public R changeStatus(Long bankId ,Byte status) {
        AdminBank adminBank = new AdminBank();
        adminBank.setId(bankId);
        adminBank.setStatus(status);
        boolean updateById = adminBankService.updateById(adminBank);
        if(updateById) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

//    @Override
//    public List<AdminBankDto> getAllAdminBanks() {
//        List<AdminBankDto> adminBankDtos = adminBankService.getAllAdminBanks() ;
//        return adminBankDtos;
//    }
}
