package com.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "com-demo-domain-Account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "account")
public class Account {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Primary Key")
    private Long id;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "User Id")
    private Long userId;

    @TableField(value = "coin_id")
    @ApiModelProperty(value = "Coin id")
    private Long coinId;

    @TableField(value = "status")
    @ApiModelProperty(value = "Status: 1-normal 2-frozen")
    private Boolean status;

    @TableField(value = "balance_amount")
    @ApiModelProperty(value = "Coin balance amount")
    private BigDecimal balanceAmount;

    @TableField(value = "freeze_amount")
    @ApiModelProperty(value = "Coin freeze amount")
    private BigDecimal freezeAmount;

    @TableField(value = "recharge_amount")
    @ApiModelProperty(value = "Accumulated recharge amount")
    private BigDecimal rechargeAmount;

    @TableField(value = "withdrawals_amount")
    @ApiModelProperty(value = "Accumulated withdrawals amount")
    private BigDecimal withdrawalsAmount;

    @TableField(value = "net_value")
    @ApiModelProperty(value = "Net value")
    private BigDecimal netValue;

    @TableField(value = "lock_margin")
    @ApiModelProperty(value = "Lock margin")
    private BigDecimal lockMargin;

    @TableField(value = "float_profit")
    @ApiModelProperty(value = "Float profit")
    private BigDecimal floatProfit;

    @TableField(value = "total_profit")
    @ApiModelProperty(value = "Total profit")
    private BigDecimal totalProfit;

    @TableField(value = "rec_addr")
    @ApiModelProperty(value = "Recharge address")
    private String recAddr;

    @TableField(value = "version")
    @ApiModelProperty(value = "Version")
    private Long version;

    @TableField(value = "last_update_time")
    @ApiModelProperty(value = "Last update time")
    private Date lastUpdateTime;

    @TableField(value = "created")
    @ApiModelProperty(value = "Created time")
    private Date created;

    @TableField(exist = false)
    @ApiModelProperty(name = "Selling price")
    private BigDecimal sellRate;

    @TableField(exist = false)
    @ApiModelProperty(name = "Buying price")
    private BigDecimal buyRate;
}