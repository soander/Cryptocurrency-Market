package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.Config;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ConfigService extends IService<Config> {
    Page<Config> findByPage(Page<Config> page, String type, String name, String code);

    Config getConfigByCode(String sign);
}