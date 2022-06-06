package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.Market;
import com.demo.dto.MarketDto;

import java.util.List;

public interface MarketService extends IService<Market>{

    // Query markets by page
    Page<Market> findByPage(Page<Market> page, Long tradeAreaId, Byte status);

    // Query markets by trade area id
    List<Market> getMarkersByTradeAreaId(Long id);

    // Query markets by symbol
    Market getMarkerBySymbol(String symbol);

    // Get Market by buy and sell coin id
    MarketDto findByCoinId(Long buyCoinId, Long sellCoinId);

    // Query all markets
    List<MarketDto> queryAllMarkets();

    // Query markets by trade area id
    List<Market> queryByAreaId(Long id);
}
