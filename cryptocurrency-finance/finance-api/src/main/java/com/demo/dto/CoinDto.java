package com.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "coin's RPC object")
public class CoinDto {

    @ApiModelProperty(value = "Coin id")
    private Long id;

    @ApiModelProperty(value = "Coin name")
    private String name;

    @ApiModelProperty(value = "Coin title")
    private String title;

    @ApiModelProperty(value = "Coin img")
    private String img;

    @ApiModelProperty(value = "Base amount")
    private BigDecimal baseAmount;

    @ApiModelProperty(value = "Minimum amount")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "Maximum amount")
    private BigDecimal maxAmount;

    @ApiModelProperty(value = "Maximum amount per day")
    private BigDecimal dayMaxAmount;
}
