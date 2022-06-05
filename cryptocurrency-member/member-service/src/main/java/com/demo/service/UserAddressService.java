package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserAddressService extends IService<UserAddress>{

    // Query user's wallet address by user id
    Page<UserAddress> findByPage(Page<UserAddress> page, Long userId);

    // Get user's wallet address by user id
    List<UserAddress> getUserAddressByUserId(Long userId);

    // Query user's some coin wallet address by coin id and user id
    UserAddress getUserAddressByUserIdAndCoinId(String userId, Long coinId);
}
