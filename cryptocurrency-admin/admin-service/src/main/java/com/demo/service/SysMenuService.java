package com.demo.service;

import com.demo.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu>{

    /**
     * get Menus By User Id
     * @param userId
     * @return
     */
    List<SysMenu> getMenusByUserId(Long userId);

}
