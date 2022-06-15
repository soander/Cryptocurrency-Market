package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.EntrustOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.ExchangeTrade;
import com.demo.param.OrderParam;
import com.demo.vo.TradeEntrustOrderVo;

public interface EntrustOrderService extends IService<EntrustOrder>{

    // Query user's entrust order list
    Page<EntrustOrder> findByPage(Page<EntrustOrder> page, Long userId, String symbol, Integer type);

    // Query user's entrust order history list
    Page<TradeEntrustOrderVo> getHistoryEntrustOrder(Page<EntrustOrder> page, String symbol, Long userId);

    // Query user's unfinished entrust order list
    Page<TradeEntrustOrderVo> getEntrustOrder(Page<EntrustOrder> page, String symbol, Long userId);

    // Create entrusted order
    Boolean createEntrustOrder(Long userId, OrderParam orderParam);

    // Update entrusted order about exchange trade
    void doMatch(ExchangeTrade exchangeTrade);

    // Cancel entrusted order
    void cancleEntrustOrder(Long orderId);

    // Cancel entrusted order to database
    void cancleEntrustOrderToDb(String orderId);
}
