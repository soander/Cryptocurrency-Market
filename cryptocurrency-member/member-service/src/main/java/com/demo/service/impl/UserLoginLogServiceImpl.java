package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.UserLoginLog;
import com.demo.mapper.UserLoginLogMapper;
import com.demo.service.UserLoginLogService;
import org.springframework.stereotype.Service;
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService{

}
