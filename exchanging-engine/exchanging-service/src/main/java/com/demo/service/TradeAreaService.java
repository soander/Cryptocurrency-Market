package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.TradeArea;
import com.demo.dto.TradeAreaDto;
import com.demo.vo.TradeAreaMarketVo;

import java.util.List;

public interface TradeAreaService extends IService<TradeArea>{

    // Query trade area data by page
    Page<TradeArea> findByPage(Page<TradeArea> page, String name, Byte status);

    // Query trade area data by status
    List<TradeArea> findAll(Byte status);

    // Query all trade area market
    List<TradeAreaMarketVo> findTradeAreaMarket();

    // Query user's favorite trade area market
    List<TradeAreaMarketVo> getUserFavoriteMarkets(Long userId);

    // Query all trade area market
    List<TradeAreaDto> findAllTradeAreaAndMarket();
}
