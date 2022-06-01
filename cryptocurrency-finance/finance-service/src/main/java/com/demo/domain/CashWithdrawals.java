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

@ApiModel(value = "com-demo-domain-CashWithdrawals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cash_withdrawals")
public class CashWithdrawals {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Primary key")
    private Long id;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "User id")
    private Long userId;

    @TableField(value = "coin_id")
    @ApiModelProperty(value = "Coin id")
    private Long coinId;

    @TableField(value = "account_id")
    @ApiModelProperty(value = "Account id")
    private Long accountId;

    @TableField(value = "num")
    @ApiModelProperty(value = "Withdrawal number")
    private BigDecimal num;

    @TableField(value = "fee")
    @ApiModelProperty(value = "Fee")
    private BigDecimal fee;

    @TableField(value = "mum")
    @ApiModelProperty(value = "Get number")
    private BigDecimal mum;

    @TableField(value = "truename")
    @ApiModelProperty(value = "Account holder")
    private String truename;

    @TableField(value = "bank")
    @ApiModelProperty(value = "Bank name")
    private String bank;

    @TableField(value = "bank_prov")
    @ApiModelProperty(value = "Bank province")
    private String bankProv;

    @TableField(value = "bank_city")
    @ApiModelProperty(value = "Bank city")
    private String bankCity;

    @TableField(value = "bank_addr")
    @ApiModelProperty(value = "Bank address")
    private String bankAddr;

    @TableField(value = "bank_card")
    @ApiModelProperty(value = "Bank card number")
    private String bankCard;

    @TableField(value = "remark")
    @ApiModelProperty(value = "Remark")
    private String remark;

    @TableField(value = "step")
    @ApiModelProperty(value = "Audit step")
    private Byte step;

    @TableField(value = "status")
    @ApiModelProperty(value = "Status: 0-Waiting audit; 1-Pass; 2-Refuse; 3-Success")
    private Byte status;

    @TableField(value = "created")
    @ApiModelProperty(value = "Created time")
    private Date created;

    @TableField(value = "last_update_time")
    @ApiModelProperty(value = "Last update time")
    private Date lastUpdateTime;

    @TableField(value = "last_time")
    @ApiModelProperty(value = "Last withdrawal time")
    private Date lastTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "User name")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "User real name")
    private String realName;
}