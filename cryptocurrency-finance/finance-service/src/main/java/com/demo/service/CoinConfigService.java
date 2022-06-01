package com.demo.service;

import com.demo.domain.CoinConfig;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CoinConfigService extends IService<CoinConfig> {

    // Query coin config information by coin id
    CoinConfig findByCoinId(Long coinId);

    // Update or save coin config
    boolean updateOrSave(CoinConfig coinConfig);
}

