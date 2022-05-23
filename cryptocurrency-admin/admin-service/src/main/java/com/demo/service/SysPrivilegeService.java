package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.SysPrivilege;

import java.util.List;

public interface SysPrivilegeService extends IService<SysPrivilege>{


    /**
     *  Get menu all privilege
     * @param roleId
     * @param menuId
     *
     * @return
     */
    List<SysPrivilege> getAllSysPrivilege(Long menuId ,Long roleId);
}
