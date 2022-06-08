package com.demo.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Market RPC DTO")
public class MarketDto {

    @ApiModelProperty(value="Market ID")
    private Long id;

    @ApiModelProperty(value="类型：1-数字货币；2：创新交易")
    private Byte type;

    @ApiModelProperty(value="Trade area id")
    @NotNull
    private Long tradeAreaId;

    @ApiModelProperty(value="Sell coin id")
    @NotBlank
    private Long sellCoinId;

    @ApiModelProperty(value="Buy coin id")
    @NotNull
    private Long buyCoinId;

    @ApiModelProperty(value="Symobl")
    @NotNull
    private String symbol;

    @ApiModelProperty(value="Name")
    private String name;

    @ApiModelProperty(value="Title")
    private String title;

    @ApiModelProperty(value="Market logo img")
    private String img;

    @ApiModelProperty(value="Open price")
    @NotNull
    private BigDecimal openPrice;

    @ApiModelProperty(value="Buy fee rate")
    @NotNull
    private BigDecimal feeBuy;

    @ApiModelProperty(value="Sell fee rate")
    @NotNull
    private BigDecimal feeSell;

    @ApiModelProperty(value="Margin ratio")
    private BigDecimal marginRate;

    @ApiModelProperty(value="Minimum entrust order amount for a single transaction")
    @NotNull
    private BigDecimal numMin;

    @ApiModelProperty(value="Maximum entrust order amount for a single transaction")
    @NotNull
    private BigDecimal numMax;

    @ApiModelProperty(value="Minimum transaction amount for a single transaction")
    private BigDecimal tradeMin;

    @ApiModelProperty(value="Maximum transaction amount for a single transaction")
    private BigDecimal tradeMax;

    @ApiModelProperty(value="Price decimal places")
    @NotNull
    private Byte priceScale;

    @ApiModelProperty(value="Number of decimal places")
    private Byte numScale;

    @ApiModelProperty(value="Contract unit")
    private Integer contractUnit;

    @ApiModelProperty(value="Point value")
    private BigDecimal pointValue;

    @ApiModelProperty(value="Merge depth: 4-0.0001; 3-0.001")
    @NotNull
    private String mergeDepth;

    @ApiModelProperty(value="Trade time")
    @NotNull
    private String tradeTime;

    @ApiModelProperty(value="Trade week")
    @NotNull
    private String tradeWeek;

    @ApiModelProperty(value="Sort")
    private Integer sort;

    @ApiModelProperty(value="Status: 0-disable; 1-enable")
    private Integer status;
}
