package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.SysRole;
import com.demo.mapper.SysPrivilegeMapper;
import com.demo.mapper.SysRoleMapper;
import com.demo.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPrivilegeMapper sysPrivilegeMapper ;

    @Override
    public SysRole getById(Serializable id) {
        SysRole sysRole = super.getById(id);
        sysRole.setPrivileges(sysPrivilegeMapper.selectByRoleId(sysRole.getId()));
        return sysRole;
    }

    @Override
    public boolean isSuperAdmin(Long userId) {
        String roleCode = sysRoleMapper.getUserRoleCode(userId);
        if (!StringUtils.isEmpty(roleCode) && roleCode.equals("ROLE_ADMIN")) {
            return true;
        }
        return false;
    }

    /**
     * @param page
     * @param name
     * @return
     */
    @Override
    public Page<SysRole> findByPage(Page<SysRole> page, String name) {
        return page(page, new LambdaQueryWrapper<SysRole>().like(
                !StringUtils.isEmpty(name),
                SysRole::getName,
                name
        ));
    }
}
