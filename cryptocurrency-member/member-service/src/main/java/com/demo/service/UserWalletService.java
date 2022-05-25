package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.UserWallet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserWalletService extends IService<UserWallet>{

    Page<UserWallet> findByPage(Page<UserWallet> page, Long userId);

    /**
     * 查询用户的提币的地址
     * @param userId
     * 用户的Id
     * @param coinId
     * 币种的Id
     * @return
     */
    List<UserWallet> findUserWallets(Long userId, Long coinId);

    /**
     * 删除用户的提现地址
     * @param addressId
     *   提现地址的Id
     * @param payPassword
     * 交易密码
     * @return
     */
    boolean deleteUserWallet(Long addressId, String payPassword);
}
