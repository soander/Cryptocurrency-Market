package com.demo.service;

import com.demo.domain.SysMenu;
import com.demo.domain.SysRolePrivilege;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.model.RolePrivilegesParam;
//import com.demo.model.RolePrivilegesParam;

import java.util.List;

public interface SysRolePrivilegeService extends IService<SysRolePrivilege> {

    /**
     * Find System Menu and Privileges
     * @param roleId
     * @return
     */
    List<SysMenu> findSysMenuAndPrivileges(Long roleId);

    /**
     * Grant Privileges
     * @param rolePrivilegesParam
     * @return
     */
    boolean grantPrivileges(RolePrivilegesParam rolePrivilegesParam);
}
