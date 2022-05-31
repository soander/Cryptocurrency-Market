package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.User;
import com.demo.domain.UserBank;
import com.demo.mapper.UserBankMapper;
import com.demo.service.UserBankService;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService{

    @Autowired
    private UserService userService;

    // Query users' bank
    @Override
    public Page<UserBank> findByPage(Page<UserBank> page, Long usrId) {
        return page(page, new LambdaQueryWrapper<UserBank>()
                .eq(usrId != null ,UserBank::getUserId ,usrId));
    }

    // Query current user's bank
    @Override
    public UserBank getCurrentUserBank(Long userId) {
        UserBank userBank = getOne(
                        new LambdaQueryWrapper<UserBank>()
                                .eq(UserBank::getUserId, userId)
                                .eq(UserBank::getStatus, 1));
        return userBank;
    }

    // Bind bank
    @Override
    public boolean bindBank(Long userId, UserBank userBank) {

       String payPassword = userBank.getPayPassword();
       User user = userService.getById(userId);
       BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
       if(!bCryptPasswordEncoder.matches(payPassword, user.getPaypassword())){
           throw new IllegalArgumentException("User pay password is not correct");
       }
        Long id = userBank.getId();
        if(id != null) {
            UserBank userBankDb = getById(id);
            if (userBankDb == null) {
                throw new IllegalArgumentException("User bank id is not correct");
            }
           return updateById(userBank); // Update value
        }

        userBank.setUserId(userId);
        return save(userBank);
    }
}
