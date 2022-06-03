package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.AdminAddress;
import com.baomidou.mybatisplus.extension.service.IService;
public interface AdminAddressService extends IService<AdminAddress>{

    // Query all admin address by page and coin id
    Page<AdminAddress> findByPage(Page<AdminAddress> page, Long coinId);
}
