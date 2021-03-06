package com.demo.config.rocket;

import com.demo.domain.ExchangeTrade;
import com.demo.service.EntrustOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Slf4j
public class ExchangeTradeListener {

    @Autowired
    private EntrustOrderService entrustOrderService;

    @Transactional
    @StreamListener("exchange_trade_in")
    public void receiveExchangeTrade(List<ExchangeTrade> exchangeTrades) {
        if (CollectionUtils.isEmpty(exchangeTrades)) {
            return;
        }
        for (ExchangeTrade exchangeTrade : exchangeTrades) {
            if (exchangeTrade != null) {
                entrustOrderService.doMatch(exchangeTrade);
            }
        }
    }

    @Transactional
    @StreamListener("cancel_order_in")
    public void receiveCancelOrder(String orderId) {
        entrustOrderService.cancleEntrustOrderToDb(orderId);
    }
}
