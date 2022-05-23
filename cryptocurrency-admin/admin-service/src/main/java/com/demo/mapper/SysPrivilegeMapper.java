package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.domain.SysPrivilege;

import java.util.List;
import java.util.Set;

public interface SysPrivilegeMapper extends BaseMapper<SysPrivilege> {

    List<SysPrivilege> selectByRoleId(Long roleId);

    Set<Long> getPrivilegesByRoleId(Long roleId);
}