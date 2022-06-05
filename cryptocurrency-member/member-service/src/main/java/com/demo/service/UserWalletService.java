package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.UserWallet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserWalletService extends IService<UserWallet>{

    // Query user wallet by userId
    Page<UserWallet> findByPage(Page<UserWallet> page, Long userId);

    // Query user coin address by coinId
    List<UserWallet> findUserWallets(Long userId, Long coinId);

    // Delete user coin address
    boolean deleteUserWallet(Long addressId, String payPassword);
}
