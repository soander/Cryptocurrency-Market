package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.UserBank;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserBankService extends IService<UserBank> {

    // Query users' bank
    Page<UserBank> findByPage(Page<UserBank> page, Long usrId);

    // Query current user's bank
    UserBank getCurrentUserBank(Long userId);

    // Bind bank
    boolean bindBank(Long userId, UserBank userBank);
}
