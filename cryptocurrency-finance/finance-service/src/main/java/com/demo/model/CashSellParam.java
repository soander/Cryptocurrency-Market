package com.demo.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * {"coinId":"1012972373649002497","mum":"99.00","num":"100","payPassword":"7fa8282ad93047a4d6fe6111c93b308a","validateCode":"111111"}
 */
@Data
@ApiModel(value = "Sell GCN params")
public class CashSellParam {

    @ApiModelProperty(value = "Coin id")
    @NotNull
    private Long coinId;

    @ApiModelProperty(value = "Sell money amount")
    @NotNull
    private BigDecimal mum;

    @ApiModelProperty(value = "Sell coin amount")
    @NotNull
    private BigDecimal num;

    @ApiModelProperty(value = "Pay password")
    @NotBlank
    private String payPassword;

    @ApiModelProperty(value = "Validate code")
    @NotBlank
    private String validateCode;
}
