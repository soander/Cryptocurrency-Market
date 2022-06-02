package com.demo.controller;

import com.demo.domain.Account;
import com.demo.feign.AccountServiceFeign;
import com.demo.model.R;
import com.demo.service.AccountService;
import com.demo.vo.SymbolAssetVo;
import com.demo.vo.UserTotalAccountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
@Api(tags = "Account controller")
public class AccountController implements AccountServiceFeign {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{coinName}")
    @ApiOperation(value = "Query user coin balance")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinName", value = "Coin name")
    })
    public R<Account> getUserAccount(@PathVariable("coinName") String coinName) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Account account = accountService.findByUserAndCoin(userId, coinName);
        return R.ok(account);
    }

    @GetMapping("/total")
    @ApiOperation(value = "计算用户的总资产")
    public R<UserTotalAccountVo> total() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserTotalAccountVo userTotalAccountVo = accountService.getUserTotalAccount(userId);
        return R.ok(userTotalAccountVo);
    }


    @GetMapping("/asset/{symbol}")
    @ApiOperation(value = "交易货币的资产")
    public R<SymbolAssetVo> getSymbolAssert(@PathVariable("symbol") String symbol) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        SymbolAssetVo symbolAssetVo = accountService.getSymbolAssert(symbol, userId);
        return R.ok(symbolAssetVo);
    }

    /**
     * 锁定用户的余额
     *
     * @param userId  用户的id
     * @param coinId  币种的id
     * @param mum     锁定的数量
     * @param type    业务类型
     * @param orderId 订单编号
     * @param fee
     */
    @Override
    public void lockUserAmount(Long userId, Long coinId, BigDecimal mum, String type, Long orderId, BigDecimal fee) {
        accountService.lockUserAmount(userId, coinId, mum, type, orderId, fee);
    }

    /**
     * 划转买入的账户余额
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    @Override
    public void transferBuyAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        accountService.transferBuyAmount(fromUserId, toUserId, coinId, amount, businessType, orderId);
    }

    /**
     * 划转出售的成功的账户余额
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    @Override
    public void transferSellAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        accountService.transferSellAmount(fromUserId, toUserId, coinId, amount, businessType, orderId);
    }
}
