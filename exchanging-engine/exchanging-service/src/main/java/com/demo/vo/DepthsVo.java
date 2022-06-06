package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel(value = "Depth Vo")
public class DepthsVo {

    @ApiModelProperty(value = "Entrusted buy order")
    private List<DepthItemVo> bids = Collections.emptyList() ;

    @ApiModelProperty(value = "Entrusted sell order")
    private List<DepthItemVo> asks = Collections.emptyList() ;

    @ApiModelProperty(value = "Current price(GCN)")
    private BigDecimal price = BigDecimal.ZERO;

    @ApiModelProperty(value = "Current price(CNY)")
    private BigDecimal cnyPrice =  BigDecimal.ZERO;
}
