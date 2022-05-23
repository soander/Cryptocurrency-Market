package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysRoleService extends IService<SysRole> {

    /**
     * If user is Super Admin
     * @param userId
     * @return
     */
    boolean isSuperAdmin(Long userId);

    /**
     * find By Page
     * @param page
     * @param name
     * @return
     */
    Page<SysRole> findByPage(Page<SysRole> page, String name);
}
