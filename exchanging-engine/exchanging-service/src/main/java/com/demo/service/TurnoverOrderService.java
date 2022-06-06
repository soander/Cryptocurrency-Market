package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.TurnoverOrder;
import com.demo.dto.TurnoverData24HDTO;

import java.util.List;

public interface TurnoverOrderService extends IService<TurnoverOrder>{

    // Query user's turnover order list
    Page<TurnoverOrder> findByPage(Page<TurnoverOrder> page, Long userId, String symbol, Integer type);

    /**
     * 获取买入的订单的成功的记录
     * @param orderId
     * @return
     */
    List<TurnoverOrder> getBuyTurnoverOrder(Long orderId,Long userId);

    /**
     * 获取卖出订单的成交记录
     * @param orderId
     * @return
     */
    List<TurnoverOrder> getSellTurnoverOrder(Long orderId,Long userId);

    // Query turnover order by symbol
    List<TurnoverOrder> findBySymbol(String symbol);

    /**
     * 查询该交易对的24 小时成交记录
     * @param symbol
     * @return
     */
    TurnoverData24HDTO query24HDealData(String symbol);
}
