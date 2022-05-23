package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.SysPrivilege;
import com.demo.model.R;
import com.demo.service.SysPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * @ClassName SysPrivilegeController
 * @Description The system privilege controller
 * @Author Yaozheng Wang
 * @Date 2022/5/20 15:00
 **/
@RestController
@RequestMapping("/privileges")
@Api(tags = "Privilege Management")
public class SysPrivilegeController {

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    // Get the privilege by pages
    @GetMapping
    @ApiOperation(value = "Query the privilege by pages")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" ,value = "Current Page"),
            @ApiImplicitParam(name = "size" ,value = "Page size"),
    })
    @PreAuthorize("hasAuthority('sys_privilege_query')")
    public R<Page<SysPrivilege>> findByPage(@ApiIgnore Page<SysPrivilege> page) {
        page.addOrder(OrderItem.desc("last_update_time")) ;
        Page<SysPrivilege> sysPrivilegePage = sysPrivilegeService.page(page);
        return R.ok(sysPrivilegePage) ;
    }

    @PostMapping
    @ApiOperation(value = "Add a privilege")
    @PreAuthorize("hasAuthority('sys_privilege_create')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysPrivilege" ,value = "sysPrivilege's json information")
    })
    public R add(@RequestBody @Validated SysPrivilege sysPrivilege) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        sysPrivilege.setCreateBy(Long.valueOf(userIdStr));
        sysPrivilege.setCreated(new Date());
        sysPrivilege.setLastUpdateTime(new Date());
        boolean save = sysPrivilegeService.save(sysPrivilege);
        if(save) {
            return R.ok("Add Success");
        }
        return  R.fail("Add Fail");
    }


    @PatchMapping
    @ApiOperation(value = "Update a privilege")
    @PreAuthorize("hasAuthority('sys_privilege_update')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysPrivilege" ,value = "sysPrivilege's json")
    })
    public R update(@RequestBody @Validated SysPrivilege sysPrivilege) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        sysPrivilege.setModifyBy(Long.valueOf(userIdStr));
        sysPrivilege.setLastUpdateTime(new Date());
        boolean save = sysPrivilegeService.updateById(sysPrivilege);
        if(save) {
            return R.ok("Update Success");
        }
        return R.fail("Update Fail");
    }
}
