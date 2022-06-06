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

@ApiModel(value="com-demo-domain-EntrustOrder")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "entrust_order")
public class EntrustOrder {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Entrust Order ID")
    private Long id;

    @TableField(value = "user_id")
    @ApiModelProperty(value="User id")
    private Long userId;

    @TableField(value = "market_id")
    @ApiModelProperty(value="Market id")
    private Long marketId;

    @TableField(value = "market_type")
    @ApiModelProperty(value="市场类型（1：币币交易，2：创新交易）")
    private Byte marketType;

    @TableField(value = "symbol")
    @ApiModelProperty(value="Symbol")
    private String symbol;

    @TableField(value = "market_name")
    @ApiModelProperty(value="Market name")
    private String marketName;

    @TableField(value = "price")
    @ApiModelProperty(value="Entrust price")
    private BigDecimal price;

    @TableField(value = "merge_low_price")
    @ApiModelProperty(value="Merge low price")
    private BigDecimal mergeLowPrice;

    @TableField(value = "merge_high_price")
    @ApiModelProperty(value="Merge high price")
    private BigDecimal mergeHighPrice;

    @TableField(value = "volume")
    @ApiModelProperty(value="Entrust volume")
    private BigDecimal volume;

    @TableField(value = "amount")
    @ApiModelProperty(value="Entrust amount")
    private BigDecimal amount;

    @TableField(value = "fee_rate")
    @ApiModelProperty(value="Fee rate")
    private BigDecimal feeRate;

    @TableField(value = "fee")
    @ApiModelProperty(value="Fee")
    private BigDecimal fee;

    @TableField(value = "contract_unit")
    @ApiModelProperty(value="Contract unit")
    private Integer contractUnit;

    @TableField(value = "deal")
    @ApiModelProperty(value="Deal number")
    private BigDecimal deal;

    @TableField(value = "freeze")
    @ApiModelProperty(value="Freeze number")
    private BigDecimal freeze;

    @TableField(value = "margin_rate")
    @ApiModelProperty(value="Margin rate")
    private BigDecimal marginRate;

    @TableField(value = "base_coin_rate")
    @ApiModelProperty(value="base coin USDT/BTC rate")
    private BigDecimal baseCoinRate;

    @TableField(value = "price_coin_rate")
    @ApiModelProperty(value="Quote currency USDT/BTC rate")
    private BigDecimal priceCoinRate;

    @TableField(value = "lock_margin")
    @ApiModelProperty(value="Locked margin")
    private BigDecimal lockMargin;

    @TableField(value = "price_type")
    @ApiModelProperty(value="Price type: 1-Market price; 2-Limited price")
    private Byte priceType;

    @TableField(value = "trade_type")
    @ApiModelProperty(value="Trade type: 1-Open a position; 2-Close a position")
    private Byte tradeType;

    @TableField(value = "type")
    @ApiModelProperty(value="Transaction type: 1-Buy; 2-Sell")
    private Byte type;

    @TableField(value = "open_order_id")
    @ApiModelProperty(value="Open order id")
    private Long openOrderId;

    @TableField(value = "status")
    @ApiModelProperty(value="Status: 0-Unfilled; 1-Filled; 2-Canceled; 4-abnormal order")
    private Byte status;

    @TableField(value = "last_update_time")
    @ApiModelProperty(value="Last update time")
    private Date lastUpdateTime;

    @TableField(value = "created")
    @ApiModelProperty(value="Created time")
    private Date created;
}