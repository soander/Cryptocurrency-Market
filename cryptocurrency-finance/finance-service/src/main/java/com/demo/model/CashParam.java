package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Cash buy coin param")
public class CashParam {

    @ApiModelProperty(value = "Coin id")
    @NotNull
    private Long coinId;

    @ApiModelProperty(value = "Coin number")
    @NotNull
    private BigDecimal num;

    @ApiModelProperty(value = "Buy coin number")
    @NotNull
    private BigDecimal mum;
}
