package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.SysMenu;
import com.demo.mapper.SysMenuMapper;
import com.demo.service.SysMenuService;
import com.demo.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        if(sysRoleService.isSuperAdmin(userId)){
            return list() ; // super admin get all menu
        }
        return sysMenuMapper.selectMenusByUserId(userId);
    }
}
