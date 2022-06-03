package com.demo.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.UserAccountFreeze;
import com.demo.mapper.UserAccountFreezeMapper;
import com.demo.service.UserAccountFreezeService;

@Service
public class UserAccountFreezeServiceImpl extends ServiceImpl<UserAccountFreezeMapper, UserAccountFreeze> implements UserAccountFreezeService {

}

