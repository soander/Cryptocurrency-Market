package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.Account;
import com.demo.vo.SymbolAssetVo;
import com.demo.vo.UserTotalAccountVo;

import java.math.BigDecimal;

public interface AccountService extends IService<Account> {

    // Query user coin balance by userId and coinName
    Account findByUserAndCoin(Long userId, String coinName);

    /**
     * 暂时锁定用户的资产
     * @param userId
     *  用户的id
     * @param coinId
     * 币种的id
     * @param mum
     * 锁定的金额
     * @param type
     *      资金流水的类型
     * @param orderId
     *      订单的Id
     * @param fee
     *  本次操作的手续费
     */
    void lockUserAmount(Long userId, Long coinId, BigDecimal mum, String type, Long orderId, BigDecimal fee);

    /**
     * 计算用户的总的资产
     * @param userId
     * @return
     */
    UserTotalAccountVo getUserTotalAccount(Long userId);

    /**
     * 统计用户交易对的资产
     * @param symbol
     *  交易对的Symbol
     * @param userId
     *      用户的Id
     * @return
     */
    SymbolAssetVo getSymbolAssert(String symbol, Long userId);

    // Transfer account amount
    Boolean transferAccountAmount(Long adminId, Long userId, Long coinId, Long orderNum, BigDecimal num, BigDecimal fee, String remark, String businessType, Byte direction);

    // Decrease account amount
    Boolean decreaseAccountAmount(Long adminId, Long userId, Long coinId, Long orderNum ,BigDecimal num, BigDecimal fee,String remark, String businessType, byte direction);

    /**
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    void transferBuyAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId);

    /**
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    void transferSellAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId);
}

