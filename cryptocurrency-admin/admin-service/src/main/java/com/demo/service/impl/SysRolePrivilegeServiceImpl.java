package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.SysMenu;
import com.demo.domain.SysPrivilege;
import com.demo.domain.SysRolePrivilege;
import com.demo.mapper.SysRolePrivilegeMapper;
import com.demo.model.RolePrivilegesParam;
import com.demo.service.SysMenuService;
import com.demo.service.SysPrivilegeService;
import com.demo.service.SysRolePrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SysRolePrivilegeServiceImpl extends ServiceImpl<SysRolePrivilegeMapper, SysRolePrivilege> implements SysRolePrivilegeService {
    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    /**
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findSysMenuAndPrivileges(Long roleId) {
        List<SysMenu> list = sysMenuService.list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        // The root menu
        List<SysMenu> rootMenus = list.stream()
                .filter(sysMenu -> sysMenu.getParentId() == null)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rootMenus)) {
            return Collections.emptyList();
        }
        // The sub menu
        List<SysMenu> subMenus = new ArrayList<>();
        for (SysMenu rootMenu : rootMenus) {
            subMenus.addAll(getChildMenus(rootMenu.getId(), roleId, list));
        }
        return subMenus;
    }

    /**
     * @param parentId
     * @param roleId
     * @return
     */
    private List<SysMenu> getChildMenus(Long parentId, Long roleId, List<SysMenu> sources) {
        List<SysMenu> childs = new ArrayList<>();
        for (SysMenu source : sources) {
            if (Objects.equals(source.getParentId(), parentId)) {
                childs.add(source);
                source.setChilds(getChildMenus(source.getId(), roleId, sources));
                List<SysPrivilege> sysPrivileges = sysPrivilegeService.getAllSysPrivilege(source.getId(), roleId);
                source.setPrivileges(sysPrivileges);
            }
        }
        return childs;
    }

    /**
     * @param rolePrivilegesParam
     * @return
     */
    @Transactional
    @Override
    public boolean grantPrivileges(RolePrivilegesParam rolePrivilegesParam) {
        Long roleId = rolePrivilegesParam.getRoleId();
        sysRolePrivilegeService.remove(new LambdaQueryWrapper<SysRolePrivilege>().eq(SysRolePrivilege::getRoleId, roleId));
        List<Long> privilegeIds = rolePrivilegesParam.getPrivilegeIds();
        if (!CollectionUtils.isEmpty(privilegeIds)) {
            List<SysRolePrivilege> sysRolePrivileges = new ArrayList<>();
            for (Long privilegeId : privilegeIds) {
                SysRolePrivilege sysRolePrivilege = new SysRolePrivilege();
                sysRolePrivilege.setRoleId(rolePrivilegesParam.getRoleId());
                sysRolePrivilege.setPrivilegeId(privilegeId);
                sysRolePrivileges.add(sysRolePrivilege);
            }
            boolean b = sysRolePrivilegeService.saveBatch(sysRolePrivileges);
            return b;
        }
        return true;
    }
}
