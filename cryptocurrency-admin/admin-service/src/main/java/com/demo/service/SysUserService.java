package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SysUserService extends IService<SysUser> {

    /**
     * find By Page
     * @param page
     * @param mobile
     * @param fullname
     * @return
     */
    Page<SysUser> findByPage(Page<SysUser> page, String mobile, String fullname);

    /**
     * add User
     * @param sysUser
     * @return
     */
    boolean addUser(SysUser sysUser);
}
