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

@ApiModel(value = "com-demo-domain-CoinRecharge")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coin_recharge")
public class CoinRecharge {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Primary key")
    private Long id;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "User id")
    private Long userId;

    @TableField(value = "coin_id")
    @ApiModelProperty(value = "Coin id")
    private Long coinId;

    @TableField(value = "coin_name")
    @ApiModelProperty(value = "Coin name")
    private String coinName;

    @TableField(value = "coin_type")
    @ApiModelProperty(value = "Coin type")
    private String coinType;

    @TableField(value = "address")
    @ApiModelProperty(value = "Wallet address")
    private String address;

    @TableField(value = "confirm")
    @ApiModelProperty(value = "Confirm recharge number")
    private Integer confirm;

    @TableField(value = "status")
    @ApiModelProperty(value = "Status: 0-Waiting recharge; 1-Recharge Fail; 2-Add to account fail; 3-Add to account success")
    private Integer status;

    @TableField(value = "txid")
    @ApiModelProperty(value = "Transaction id")
    private String txid;

    @TableField(value = "amount")
    @ApiModelProperty(value = "")
    private BigDecimal amount;

    @TableField(value = "last_update_time")
    @ApiModelProperty(value = "Last update time")
    private Date lastUpdateTime;

    @TableField(value = "created")
    @ApiModelProperty(value = "Created time")
    private Date created;

    @TableField(exist = false)
    @ApiModelProperty(value = "User name")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "User real name")
    private String realName;
}