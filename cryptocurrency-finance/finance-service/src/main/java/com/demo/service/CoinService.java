package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.Coin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.dto.CoinDto;

import java.util.List;

public interface CoinService extends IService<Coin> {

    // Query coin list
    Page<Coin> findByPage(String name, String type, Byte status, String title, String walletType, Page<Coin> page);

    // Query all coin by status
    List<Coin> getCoinsByStatus(Byte status);

    // Query coin by coin name
    Coin getCoinByCoinName(String coinName);

    // Query coin list by coin ids
    List<CoinDto> findList(List<Long> coinIds);
}

