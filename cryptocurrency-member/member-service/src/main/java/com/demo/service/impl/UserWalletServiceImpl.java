package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.User;
import com.demo.domain.UserWallet;
import com.demo.mapper.UserWalletMapper;
import com.demo.service.UserService;
import com.demo.service.UserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService{

    @Autowired
    private UserService userService;

    @Override
    public Page<UserWallet> findByPage(Page<UserWallet> page, Long userId) {
        return page(page, new LambdaQueryWrapper<UserWallet>().eq(UserWallet::getUserId ,userId));
    }

    /**
     * @Author Yaozheng Wang
     * @Description Query user coin address by coinId
     * @Date 2022/6/3 13:55
     **/
    @Override
    public List<UserWallet> findUserWallets(Long userId, Long coinId) {
        return list(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUserId,userId)
                .eq(UserWallet::getCoinId,coinId)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Add user coin address
    * @Date 2022/6/3 13:58
    **/
    @Override
    public boolean save(UserWallet entity) {
        Long userId = entity.getUserId();
        User user = userService.getById(userId);
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        String payPassword = user.getPaypassword();
        if(StringUtils.isEmpty(payPassword) || !(new BCryptPasswordEncoder().matches(entity.getPayPassword(),payPassword))) {
            throw new IllegalArgumentException("Pay password is not correct");
        }
        return super.save(entity);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Delete user coin address
    * @Date 2022/6/3 14:03
    **/
    @Override
    public boolean deleteUserWallet(Long addressId, String payPassword) {
        UserWallet userWallet = getById(addressId);
        if(userWallet == null) {
            throw new IllegalArgumentException("Wallet not found");
        }
        Long userId = userWallet.getUserId();
        User user = userService.getById(userId);
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        String paypassword = user.getPaypassword();
        if(StringUtils.isEmpty(paypassword) ||  !(new BCryptPasswordEncoder().matches(payPassword,paypassword))){
            throw new IllegalArgumentException("Pay password is not correct");
        }
        return super.removeById(addressId);
    }
}
