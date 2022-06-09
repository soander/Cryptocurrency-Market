package com.demo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Entrusted order operation")
public class OrderParam {

    @NotBlank
    @ApiModelProperty(value = "Symbol")
    private String symbol;

    @NotNull
    @ApiModelProperty(value = "Price")
    private BigDecimal price;

    @NotNull
    @ApiModelProperty(value = "Volume")
    private BigDecimal volume;

    @NotNull
    @ApiModelProperty(value = "Type: 1-Buy; 2-Sell")
    private Integer type;
}
