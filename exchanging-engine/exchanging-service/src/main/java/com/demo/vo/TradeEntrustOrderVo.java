package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "unfinished entrust trade order")
public class TradeEntrustOrderVo {

    @ApiModelProperty(value = "Entrust order id")
    private Long orderId;

    @ApiModelProperty(value = "Entrust order type")
    private Integer type;

    @ApiModelProperty(value = "Entrust price")
    private BigDecimal price;

    @ApiModelProperty(value = "Average deal's sell price")
    private BigDecimal dealAvgPrice;

    @ApiModelProperty(value = "Entrust order volume")
    private BigDecimal volume;

    @ApiModelProperty(value = "Deal volume")
    private BigDecimal dealVolume;

    @ApiModelProperty(value = "Estimated deal amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "Total deal amount")
    private BigDecimal dealAmount;

    @ApiModelProperty(value = "Status")
    private Integer status;

    @ApiModelProperty(value = "Created time")
    private Date created;
}
