package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.SysRole;
import com.demo.model.R;
import com.demo.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * @ClassName SysRoleController
 * @Description The system role controller
 * @Author Yaozheng Wang
 * @Date 2022/5/20 15:59
 **/
@RestController
@RequestMapping("/roles")
@Api(tags = "Role Management")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping
    @ApiOperation(value = "条件分页查询")
    @PreAuthorize("hasAuthority('sys_role_query')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的大小"),
            @ApiImplicitParam(name = "name", value = "角色名称"),
    })
    public R<Page<SysRole>> findByPage(@ApiIgnore Page<SysRole> page, String name) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return R.ok(sysRoleService.findByPage(page, name));
    }


    @PostMapping
    @ApiOperation(value = "Add a new role")
    @PreAuthorize("hasAuthority('sys_role_create')")
    public R add(@RequestBody @Validated SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return R.ok();
        }
        return R.fail("Add fail");

    }

    @PostMapping("/delete")
    @ApiOperation(value = "Delete a role")
    @PreAuthorize("hasAuthority('sys_role_delete')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "Delete role id collection")
    })
    public R delete(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return R.fail("Delte object can't null");
        }
        boolean b = sysRoleService.removeByIds(Arrays.asList(ids));
        if (b) {
            return R.ok();
        }
        return R.fail("Delete fail");
    }

}
