package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.SysPrivilege;
import com.demo.mapper.SysPrivilegeMapper;
import com.demo.service.SysPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class SysPrivilegeServiceImpl extends ServiceImpl<SysPrivilegeMapper, SysPrivilege> implements SysPrivilegeService {

    @Autowired
    private SysPrivilegeMapper sysPrivilegeMapper;
    /**
     * @param menuId
     * @param roleId
     * @return
     */
    @Override
    public List<SysPrivilege> getAllSysPrivilege(Long menuId, Long roleId) {

        List<SysPrivilege> sysPrivileges = list(new LambdaQueryWrapper<SysPrivilege>().eq(SysPrivilege::getMenuId, menuId));
        if(CollectionUtils.isEmpty(sysPrivileges)){
            return Collections.emptyList();
        }

        for (SysPrivilege sysPrivilege : sysPrivileges) {
            Set<Long>  currentRoleSysPrivilegeIds = sysPrivilegeMapper.getPrivilegesByRoleId(roleId);
            if (currentRoleSysPrivilegeIds.contains(sysPrivilege.getId())){
                sysPrivilege.setOwn(1);
            }
        }
        return sysPrivileges;
    }
}
