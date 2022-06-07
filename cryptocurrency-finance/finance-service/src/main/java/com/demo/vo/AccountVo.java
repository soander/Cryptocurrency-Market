package com.demo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@ApiModel(value = "Account Vo")
public class AccountVo {

    private Long id; // Account id

    private Long userId; // User id

    private Long coinId; // Coin id

    private Integer accountStatus; // Account status: 1-normal, 2-frozen

    private BigDecimal balanceAmount; // Account balance amount

    private BigDecimal carryingAmount; // Carrying amount

    private BigDecimal freezeAmount; // Freeze amount

    private BigDecimal rechargeAmount; // Accumulated recharge amount

    private BigDecimal withdrawalsAmount; // Accumulated withdrawals amount

    private BigDecimal netValue; // Net value

    private BigDecimal lockMargin; // Locked margin

    private BigDecimal floatProfit; // Float profit

    private BigDecimal totalProfit; // Total profit

    private String recAddr; // Recharge address

    private Integer version; // Version

    private Date updateTime; // Update time

    private Date createTime; // Create time

    private BigDecimal positionGainAndLossSummary; // Profit and loss summary of position

    private String coinType; // Coin type

    private String coinName; // Coin name

    private String coinImgUrl; // Coin img url

    private BigDecimal coinNum; // The number of remaining coins

    private BigDecimal totalNum; // Total coin num

    private BigDecimal coinFreezeNum; // Coin freeze amount

    private BigDecimal feeRate; // Withdrawal fee rate

    private BigDecimal minFeeNum; // Minimum withdrawal fee

    private int withdrawFlag; // Withdrawal switch: 0-off, 1-on

    private int rechargeFlag; // Recharge switch: 0-off, 1-on

    private BigDecimal totalCny; // Total CNY

    private BigDecimal currentPrice; // Current base coin price

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setCarryingAmount(BigDecimal carryingAmount) {
        this.carryingAmount = carryingAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setWithdrawalsAmount(BigDecimal withdrawalsAmount) {
        this.withdrawalsAmount = withdrawalsAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue.setScale(8, RoundingMode.HALF_UP);
    }

    public void setLockMargin(BigDecimal lockMargin) {
        this.lockMargin = lockMargin.setScale(8, RoundingMode.HALF_UP);
    }

    public void setFloatProfit(BigDecimal floatProfit) {
        this.floatProfit = floatProfit.setScale(8, RoundingMode.HALF_UP);
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit.setScale(8, RoundingMode.HALF_UP);
    }
}
