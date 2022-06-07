package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "Symbol asset vo")
public class SymbolAssetVo {

    @ApiModelProperty(value = "Buy coin balance")
    private BigDecimal buyAmount;

    @ApiModelProperty(value = "Buy coin unit")
    private String buyUnit;

    @ApiModelProperty(value = "Buy coin frozen amount")
    private BigDecimal buyLockAmount;

    @ApiModelProperty(value = "Buy fee rate")
    private BigDecimal buyFeeRate;

    @ApiModelProperty(value = "Sell coin balance")
    private BigDecimal sellAmount;

    @ApiModelProperty(value = "Sell coin unit")
    private String sellUnit;

    @ApiModelProperty(value = "Sell coin frozen amount")
    private BigDecimal sellLockAmount;

    @ApiModelProperty(value = "Sell fee rate")
    private BigDecimal sellFeeRate;
}
