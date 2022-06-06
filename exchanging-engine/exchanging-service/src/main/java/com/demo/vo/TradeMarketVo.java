package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "交易市场")
@Data
public class TradeMarketVo {

    @ApiModelProperty(value = "Trade symbol")
    private String symbol;

    @ApiModelProperty(value = "Trade market name")
    private String name;

    @ApiModelProperty(value = "Trade image")
    private String image;

    @ApiModelProperty(value = "Buy fee rate")
    private BigDecimal buyFeeRate;

    @ApiModelProperty(value = "Sell fee rate")
    private BigDecimal sellFeeRate;

    @ApiModelProperty(value = "Price Decimal")
    private int priceScale;

    @ApiModelProperty(value = "Price Decimal numbers")
    private int numScale;

    @ApiModelProperty(value = " Minimum amount")
    private BigDecimal numMin;

    /**
     * 最大的委托量
     */
    @ApiModelProperty(value = " 最大小数位数")
    private BigDecimal numMax;

    @ApiModelProperty(value = "Minimum trade amount")
    private BigDecimal tradeMin;

    @ApiModelProperty(value = "Maximum trade amount")
    private BigDecimal tradeMax;

    @ApiModelProperty(value = "Current price")
    private BigDecimal price;

    @ApiModelProperty(value = "Price unity")
    private String priceUnit;

    @ApiModelProperty(value = "Current RMB price")
    private BigDecimal cnyPrice;

    @ApiModelProperty(value = "Coin's RMB price")
    private BigDecimal coinCnyPrice;

    @ApiModelProperty(value = "Merge depth")
    private List<MergeDeptVo> mergeDepth;

    @ApiModelProperty(value = "highest price")
    private BigDecimal high;

    @ApiModelProperty(value = "lowest price")
    private BigDecimal low;

    @ApiModelProperty(value = "total daily trading volume")
    private BigDecimal volume = BigDecimal.ZERO;

    @ApiModelProperty(value = "Total trade amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @ApiModelProperty(value = "Price change")
    private double change;

    @ApiModelProperty(value = "Sort")
    private int sort;
}
