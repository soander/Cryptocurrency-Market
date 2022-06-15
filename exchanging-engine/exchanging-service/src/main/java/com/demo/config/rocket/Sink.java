package com.demo.config.rocket;

import com.demo.enums.MessageChannel;
import org.springframework.cloud.stream.annotation.Input;

public interface Sink {

    // Input exchange trade data
    @Input("exchange_trade_in")
    MessageChannel exchangeTradeIn();

    // Input cancel order data
    @Input("cancel_order_in")
    MessageChannel cancelOrderIn();
}
