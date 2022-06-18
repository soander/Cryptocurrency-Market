package com.demo.model;

import com.demo.enums.OrderDirection;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeTrade {

    private String symbol;

    private OrderDirection direction;

    private BigDecimal price;

    private BigDecimal amount;

    private String buyOrderId;

    private String sellOrderId;

    private BigDecimal buyTurnover;

    private BigDecimal sellTurnover;

    private Long time;
}
