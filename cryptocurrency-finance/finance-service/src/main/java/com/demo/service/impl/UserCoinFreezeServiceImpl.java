package com.demo.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.UserCoinFreeze;
import com.demo.mapper.UserCoinFreezeMapper;
import com.demo.service.UserCoinFreezeService;

@Service
public class UserCoinFreezeServiceImpl extends ServiceImpl<UserCoinFreezeMapper, UserCoinFreeze> implements UserCoinFreezeService {

}

