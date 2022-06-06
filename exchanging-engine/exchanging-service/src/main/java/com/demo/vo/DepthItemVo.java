package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel(value = "Depth Item Vo")
@NoArgsConstructor
@AllArgsConstructor
public class DepthItemVo {

    @ApiModelProperty(value = "Price")
    private BigDecimal price = BigDecimal.ZERO;

    @ApiModelProperty(value = "Amount")
    private BigDecimal volume = BigDecimal.ZERO;
}
