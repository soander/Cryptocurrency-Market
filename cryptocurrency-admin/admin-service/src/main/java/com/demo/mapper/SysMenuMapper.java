package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.domain.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    // User id get menu
    List<SysMenu> selectMenusByUserId(Long userId);

    List<SysMenu> selectMenusByRoleId(Long roleId);
}