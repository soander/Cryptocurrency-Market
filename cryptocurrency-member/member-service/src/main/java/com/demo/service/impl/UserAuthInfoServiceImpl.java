package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.UserAuthInfo;
import com.demo.mapper.UserAuthInfoMapper;
import com.demo.service.UserAuthInfoService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements UserAuthInfoService{

    @Override
    public List<UserAuthInfo> getUserAuthInfoByCode(Long authCode) {
        return list(new LambdaQueryWrapper<UserAuthInfo>().eq(UserAuthInfo::getAuthCode,authCode));
    }

    @Override
    public List<UserAuthInfo> getUserAuthInfoByUserId(Long id) {
        List<UserAuthInfo> list = list(new LambdaQueryWrapper<UserAuthInfo>()
                .eq(UserAuthInfo::getUserId, id)
                .orderByDesc(UserAuthInfo::getCreated)
                .last("limit 3"));
        return list == null ? Collections.emptyList() : list;
    }
}
