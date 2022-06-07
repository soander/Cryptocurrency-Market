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
    @ApiOperation(value = "User total account balance")
    public R<UserTotalAccountVo> total() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserTotalAccountVo userTotalAccountVo = accountService.getUserTotalAccount(userId);
        return R.ok(userTotalAccountVo);
    }

    @GetMapping("/asset/{symbol}")
    @ApiOperation(value = "Get symbol assert balance")
    public R<SymbolAssetVo> getSymbolAssert(@PathVariable("symbol") String symbol) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        SymbolAssetVo symbolAssetVo = accountService.getSymbolAssert(symbol, userId);
        return R.ok(symbolAssetVo);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Lock user account balance
    * @Date 2022/6/7 10:26
    **/
    @Override
    public void lockUserAmount(Long userId, Long coinId, BigDecimal mum, String type, Long orderId, BigDecimal fee) {
        accountService.lockUserAmount(userId, coinId, mum, type, orderId, fee);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Transfer user buy amount to account balance
    * @Date 2022/6/7 10:26
    **/
    @Override
    public void transferBuyAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        accountService.transferBuyAmount(fromUserId, toUserId, coinId, amount, businessType, orderId);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Transfer user sell amount to account balance
    * @Date 2022/6/7 10:27
    **/
    @Override
    public void transferSellAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        accountService.transferSellAmount(fromUserId, toUserId, coinId, amount, businessType, orderId);
    }
}
