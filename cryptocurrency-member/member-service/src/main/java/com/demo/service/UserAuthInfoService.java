package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.UserAuthInfo;

import java.util.List;

public interface UserAuthInfoService extends IService<UserAuthInfo>{

    List<UserAuthInfo> getUserAuthInfoByCode(Long authCode);

    List<UserAuthInfo> getUserAuthInfoByUserId(Long id);
}
