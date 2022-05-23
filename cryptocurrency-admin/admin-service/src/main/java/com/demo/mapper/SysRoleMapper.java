package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.domain.SysRole;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    // Get user role code
    String getUserRoleCode(Long userId);
}