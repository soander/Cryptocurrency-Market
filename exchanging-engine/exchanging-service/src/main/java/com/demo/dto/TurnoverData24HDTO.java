package com.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
public class TurnoverData24HDTO {

    // 24 hours volume
    private BigDecimal volume = BigDecimal.ZERO;

    // 24 hours total turnover
    private BigDecimal amount = BigDecimal.ZERO;
}
