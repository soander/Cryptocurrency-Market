package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "Total account balance")
public class UserTotalAccountVo {

    @ApiModelProperty(value = "CNY balance")
    private BigDecimal amount;

    @ApiModelProperty(value = "USDT balance")
    private BigDecimal amountUs;

    @ApiModelProperty(value = "USDT unit")
    private String amountUsUnit;

    @ApiModelProperty(value = "Asset list")
    private List<AccountVo> assetList;
}
