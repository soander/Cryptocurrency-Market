package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

// Coin entity
@ApiModel(value = "com-demo-domain-Coin")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coin")
public class Coin {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Coin id")
    private Long id;

    @TableField(value = "name")
    @ApiModelProperty(value = "Coin name")
    @NotBlank
    private String name;

    @TableField(value = "title")
    @ApiModelProperty(value = "Coin title")
    private String title;

    @TableField(value = "img")
    @ApiModelProperty(value = "Coin logo")
    @NotBlank
    private String img;

    @TableField(value = "type")
    @ApiModelProperty(value = "RMB, ETH,ethToken, default")
    @NotBlank
    private String type;

    /**
     * rgb：认购币
     * qbb：钱包币
     */
    @TableField(value = "wallet")
    @ApiModelProperty(value = "rgb：认购币,qbb：钱包币,")
    @NotBlank
    private String wallet;

    @TableField(value = "round")
    @ApiModelProperty(value = "decimal places")
    @NotNull
    private Byte round;

    /**
     * 最小提现单位
     */
    @TableField(value = "base_amount")
    @ApiModelProperty(value = "最小提现单位")
    @NotNull
    private BigDecimal baseAmount;

    @TableField(value = "min_amount")
    @ApiModelProperty(value = "Minimum withdrawal amount in a single transaction")
    @NotNull
    private BigDecimal minAmount;

    @TableField(value = "max_amount")
    @ApiModelProperty(value = "Maximum withdrawal amount in a single transaction")
    @NotNull
    private BigDecimal maxAmount;

    @TableField(value = "day_max_amount")
    @ApiModelProperty(value = "Daily maximum withdrawal amount")
    @NotNull
    private BigDecimal dayMaxAmount;

    @TableField(value = "status")
    @ApiModelProperty(value = "status: 1: enabled, 0: disabled")
    private Byte status;

    @TableField(value = "auto_out")
    @ApiModelProperty(value = "Automatic transfer out amount")
    private Double autoOut;

    @TableField(value = "rate")
    @ApiModelProperty(value = "Fee rate")
    @NotNull
    private Double rate;

    @TableField(value = "min_fee_num")
    @ApiModelProperty(value = "Minimum fee")
    @NotNull
    private BigDecimal minFeeNum;

    @TableField(value = "withdraw_flag")
    @ApiModelProperty(value = "Withdraw flag")
    @NotNull
    private Byte withdrawFlag;

    @TableField(value = "recharge_flag")
    @ApiModelProperty(value = "Recharge flag")
    @NotNull
    private Byte rechargeFlag;

    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "Last update time")
    private Date lastUpdateTime;

    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "Created time")
    private Date created;
}