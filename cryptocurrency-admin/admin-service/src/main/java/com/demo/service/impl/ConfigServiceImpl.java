package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.Config;
import com.demo.mapper.ConfigMapper;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Override
    public Page<Config> findByPage(Page<Config> page, String type, String name, String code) {
        return page(page,new LambdaQueryWrapper<Config>()
                .like(!StringUtils.isEmpty(type),Config::getType,type)
                .like(!StringUtils.isEmpty(name),Config::getName,name)
                .like(!StringUtils.isEmpty(code),Config::getCode,code)
        );
    }
}
