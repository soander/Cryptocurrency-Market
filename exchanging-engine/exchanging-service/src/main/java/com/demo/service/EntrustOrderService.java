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


    /**
     * 更新我们的委托单的数据
     * @param exchangeTrade
     */
    void doMatch(ExchangeTrade exchangeTrade);

    /**
     * 将数据投递到MQ 里面
     * @param orderId
     */
    void cancleEntrustOrder(Long orderId);

    /**
     * 数据库里面委托单的取消
     * @param orderId
     */
    void cancleEntrustOrderToDb(String orderId);
}
