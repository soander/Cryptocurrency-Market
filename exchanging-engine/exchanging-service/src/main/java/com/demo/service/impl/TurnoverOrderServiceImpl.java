package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.TurnoverOrderMapper;
import com.demo.service.TurnoverOrderService;
import com.demo.domain.TurnoverOrder;
import com.demo.dto.TurnoverData24HDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TurnoverOrderServiceImpl extends ServiceImpl<TurnoverOrderMapper, TurnoverOrder> implements TurnoverOrderService {

    /**
    * @Author Yaozheng Wang
    * @Description Query user's turnover order list
    * @Date 2022/6/5 12:42
    **/
    @Override
    public Page<TurnoverOrder> findByPage(Page<TurnoverOrder> page, Long userId, String symbol, Integer type) {
//        return page(page,new LambdaQueryWrapper<TurnoverOrder>().eq(TurnoverOrder::getus));
        return null;
    }


    /**
     * 获取买入的订单的成功的记录
     *
     * @param orderId
     * @return
     */
    @Override
    public List<TurnoverOrder> getBuyTurnoverOrder(Long orderId, Long userId) {
        return list(new LambdaQueryWrapper<TurnoverOrder>().eq(TurnoverOrder::getOrderId, orderId)
                .eq(TurnoverOrder::getBuyUserId, userId)
        );
    }


    /**
     * 获取卖出订单的成交记录
     *
     * @param orderId
     * @return
     */
    @Override
    public List<TurnoverOrder> getSellTurnoverOrder(Long orderId,Long userId) {
        return list(new LambdaQueryWrapper<TurnoverOrder>().eq(TurnoverOrder::getOrderId, orderId)
                .eq(TurnoverOrder::getSellUserId, userId)
        );

    }

    /**
    * @Author Yaozheng Wang
    * @Description Query turnover order by symbol
    * @Date 2022/6/6 11:08
    **/
    @Override
    public List<TurnoverOrder> findBySymbol(String symbol) {
        List<TurnoverOrder> turnoverOrders = list(new LambdaQueryWrapper<TurnoverOrder>()
                .eq(TurnoverOrder::getSymbol, symbol)
                .orderByDesc(TurnoverOrder::getCreated)
                .eq(TurnoverOrder::getStatus,1)
                .last("limit 60")
        );
        return turnoverOrders;
    }

    /**
     * 查询该交易对的24 小时成交记录
     *
     * @param symbol
     * @return
     */
    @Override
    public TurnoverData24HDTO query24HDealData(String symbol) {
        TurnoverData24HDTO turnoverData24HDTO = new TurnoverData24HDTO();
        turnoverData24HDTO.setAmount(BigDecimal.valueOf(1000)) ;
        turnoverData24HDTO.setVolume(BigDecimal.valueOf(100000)) ;
        return turnoverData24HDTO;
    }
}
