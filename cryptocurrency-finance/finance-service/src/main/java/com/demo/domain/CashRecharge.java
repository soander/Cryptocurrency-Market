package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "com-demo-domain-CashRecharge")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cash_recharge")
public class CashRecharge {

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
    @ApiModelProperty(value = "Coin name: CNY RMB")
    private String coinName;

    @TableField(value = "num")
    @ApiModelProperty(value = "Recharge amount")
    private BigDecimal num;

    @TableField(value = "fee")
    @ApiModelProperty(value = "Fee")
    private BigDecimal fee;

    @TableField(value = "feecoin")
    @ApiModelProperty(value = "Coin Fee")
    private String feecoin;

    @TableField(value = "mum")
    @ApiModelProperty(value = "Transaction amount")
    private BigDecimal mum;

    @TableField(value = "type")
    @ApiModelProperty(value = "Type: alipay; cai1pay; bank")
    private String type;

    @TableField(value = "tradeno")
    @ApiModelProperty(value = "Recharge trade number")
    private String tradeno;

    @TableField(value = "outtradeno")
    @ApiModelProperty(value = "Third party trade number")
    private String outtradeno;

    @TableField(value = "remark")
    @ApiModelProperty(value = "Transaction remarks")
    private String remark;

    @TableField(value = "audit_remark")
    @ApiModelProperty(value = "Audit remarks")
    private String auditRemark;

    @TableField(value = "step")
    @ApiModelProperty(value = "Current Audit step")
    private Byte step;

    @TableField(value = "status")
    @ApiModelProperty(value = "Status: 0-Waiting audit; 1-Pass; 2-Refuse; 3-Recharge success;")
    private Byte status;

    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "Created time")
    private Date created;

    @TableField(value = "last_update_time",fill=FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "Last update time")
    private Date lastUpdateTime;

    @TableField(value = "name")
    @ApiModelProperty(value = "Bank account name")
    private String name;

    @TableField(value = "bank_name")
    @ApiModelProperty(value = "Bank name")
    private String bankName;

    @TableField(value = "bank_card")
    @ApiModelProperty(value = "Bank card number")
    private String bankCard;

    @TableField(value = "last_time")
    @ApiModelProperty(value = "Last time")
    private Date lastTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "User name")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "User real name")
    private String realName;
}