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

@ApiModel(value = "com-demo-domain-AccountDetail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "account_detail")
public class AccountDetail {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
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

    /**
     * 该笔流水资金关联方的账户id
     */
    @TableField(value = "ref_account_id")
    @ApiModelProperty(value = "该笔流水资金关联方的账户id")
    private Long refAccountId;

    @TableField(value = "order_id")
    @ApiModelProperty(value = "Order id")
    private Long orderId;

    @TableField(value = "direction")
    @ApiModelProperty(value = "1-Receive 2-Transfer")
    private Byte direction;

    /**
     * 业务类型:
     * 充值(recharge_into)
     * 提现审核通过(withdrawals_out)
     * 下单(order_create)
     * 成交(order_turnover)
     * 成交手续费(order_turnover_poundage)
     * 撤单(order_cancel)
     * 注册奖励(bonus_register)
     * 提币冻结解冻(withdrawals)
     * 充人民币(recharge)
     * 提币手续费(withdrawals_poundage)
     * 兑换(cny_btcx_exchange)
     * 奖励充值(bonus_into)
     * 奖励冻结(bonus_freeze)
     */
    @TableField(value = "business_type")
    @ApiModelProperty(value = "Business Type: recharge_into, withdrawals_out, order_create, " +
            "order_turnover, order_turnover_poundage, order_cancel, bonus_register, " +
            "withdrawals, recharge, withdrawals_poundage, cny_btcx_exchange, bonus_into, bonus_freeze")
    private String businessType;

    @TableField(value = "amount")
    @ApiModelProperty(value = "Account amount")
    private BigDecimal amount;

    @TableField(value = "fee")
    @ApiModelProperty(value = "Fee")
    private BigDecimal fee;

    @TableField(value = "remark")
    @ApiModelProperty(value = "Status: recharge, withdraw, freeze, thaw, transfer out, transfer in")
    private String remark;

    @TableField(value = "created")
    @ApiModelProperty(value = "Created time")
    private Date created;

    @TableField(exist = false)
    @ApiModelProperty(value = "User name")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "User real name")
    private String realName;


    public AccountDetail(Long userId, Long coinId, Long accountId, Long refAccountId,
                         Long orderId, Integer direction, String businessType,
                         BigDecimal amount, BigDecimal fee, String remark) {
        this.userId = userId;
        this.coinId = coinId;
        this.accountId = accountId;
        this.refAccountId = refAccountId;
        this.orderId = orderId;
        this.direction = direction.byteValue();
        this.businessType = businessType;
        this.amount = amount;
        this.fee = fee;
        this.remark = remark;
    }
}