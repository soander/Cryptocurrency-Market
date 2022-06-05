package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.UserAddress;
import com.demo.mapper.UserAddressMapper;
import com.demo.service.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService{

    /**
    * @Author Yaozheng Wang
    * @Description Query user's wallet address by user id
    * @Date 2022/6/5 15:35
    **/
    @Override
    public Page<UserAddress> findByPage(Page<UserAddress> page, Long userId) {
        return page(page ,new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId,userId)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get user's wallet address by user id
    * @Date 2022/6/5 15:35
    **/
    @Override
    public List<UserAddress> getUserAddressByUserId(Long userId) {

        return list(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId,userId)
                .orderByDesc(UserAddress::getCreated)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query user's some coin wallet address by coin id and user id
    * @Date 2022/6/5 15:34
    **/
    @Override
    public UserAddress getUserAddressByUserIdAndCoinId(String userId, Long coinId) {
        return getOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId,userId)
                .eq(UserAddress::getCoinId,coinId)
        );
    }
}
