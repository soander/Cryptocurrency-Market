package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.Account;
import com.demo.vo.SymbolAssetVo;
import com.demo.vo.UserTotalAccountVo;

import java.math.BigDecimal;

public interface AccountService extends IService<Account> {

    // Query user coin balance by userId and coinName
    Account findByUserAndCoin(Long userId, String coinName);

    // Lock user's coin balance
    void lockUserAmount(Long userId, Long coinId, BigDecimal mum, String type, Long orderId, BigDecimal fee);

    // Get user total account balance
    UserTotalAccountVo getUserTotalAccount(Long userId);

    // Get symbol assert balance
    SymbolAssetVo getSymbolAssert(String symbol, Long userId);

    // Transfer account amount
    Boolean transferAccountAmount(Long adminId, Long userId, Long coinId, Long orderNum, BigDecimal num, BigDecimal fee, String remark, String businessType, Byte direction);

    // Decrease account amount
    Boolean decreaseAccountAmount(Long adminId, Long userId, Long coinId, Long orderNum ,BigDecimal num, BigDecimal fee,String remark, String businessType, byte direction);

    // Buy amount
    void transferBuyAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId);

    // Sell amount
    void transferSellAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId);
}

