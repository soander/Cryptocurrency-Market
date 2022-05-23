package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.AdminBank;
//import com.demo.dto.AdminBankDto;


public interface AdminBankService extends IService<AdminBank>{

    // Select back card by page
    Page<AdminBank> findByPage(Page<AdminBank> page, String bankCard);
//
//    List<AdminBankDto> getAllAdminBanks();
}
