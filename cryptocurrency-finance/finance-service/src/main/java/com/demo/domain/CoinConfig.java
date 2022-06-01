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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 币种配置信息
 */
@ApiModel(value = "com-demo-domain-CoinConfig")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coin_config")
public class CoinConfig {

    // Coin id
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "Coin ID")
    @NotNull
    private Long id;

    // Coin name
    @TableField(value = "name")
    @ApiModelProperty(value = "Coin name")
    private String name;

    @TableField(value = "coin_type")
    @ApiModelProperty(value = "btc; eth; ethToken; etc; \r\n\r\n")
    private String coinType;

    @TableField(value = "credit_limit")
    @ApiModelProperty(value = "Credit limit of wallet")
    @NotNull
    private BigDecimal creditLimit;

    @TableField(value = "credit_max_limit")
    @ApiModelProperty(value = "credit max limit of wallet")
    private BigDecimal creditMaxLimit;

    @TableField(value = "rpc_ip")
    @ApiModelProperty(value = "rpc ip")
    @NotBlank
    private String rpcIp;

    @TableField(value = "rpc_port")
    @ApiModelProperty(value = "rpc port")
    @NotBlank
    private String rpcPort;

    @TableField(value = "rpc_user")
    @ApiModelProperty(value = "rpc user")
    private String rpcUser;

    @TableField(value = "rpc_pwd")
    @ApiModelProperty(value = "rpc password")
    private String rpcPwd;

    @TableField(value = "last_block")
    @ApiModelProperty(value = "Last block")
    private String lastBlock;

    @TableField(value = "wallet_user")
    @ApiModelProperty(value = "Wallet user")
    private String walletUser;

    @TableField(value = "wallet_pass")
    @ApiModelProperty(value = "Wallet password")
    private String walletPass;

    @TableField(value = "contract_address")
    @ApiModelProperty(value = "Contract address")
    private String contractAddress;

    @TableField(value = "context")
    @ApiModelProperty(value = "context")
    private String context;

    @TableField(value = "min_confirm")
    @ApiModelProperty(value = "Min confirm amount")
    private Integer minConfirm;

    @TableField(value = "task")
    @ApiModelProperty(value = "Task")
    private String task;

    @TableField(value = "status")
    @ApiModelProperty(value = "0: disable; 1: enable")
    private Integer status;

    @TableField(value = "auto_draw_limit")
    @ApiModelProperty(value = "")
    private BigDecimal autoDrawLimit;

    @TableField(value = "auto_draw")
    @ApiModelProperty(value = "")
    private Integer autoDraw;
}