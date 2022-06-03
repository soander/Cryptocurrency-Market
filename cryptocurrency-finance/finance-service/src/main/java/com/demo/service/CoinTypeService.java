package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.CoinType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CoinTypeService extends IService<CoinType> {

    // Query Coin Type List
    Page<CoinType> findByPage(Page<CoinType> page, String code);

    // Query Coin Type List by status
    List<CoinType> listByStatus(Byte status);
}

