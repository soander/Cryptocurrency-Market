package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "Cash Trade Vo")
public class CashTradeVo {

    @ApiModelProperty(value = "Receipt user name")
    private String name;

    @ApiModelProperty(value = "Receipt bank name")
    private String bankName;

    @ApiModelProperty(value = "Bank card number")
    private String bankCard;

    @ApiModelProperty(value = "Cash amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "Remark")
    private String remark;

    @ApiModelProperty(value = "Status")
    private Byte status;
}
