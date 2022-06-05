package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.UserWallet;
import com.demo.model.R;
import com.demo.service.UserWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@Api(tags = "User query coin wallet address")
@RequestMapping("/userWallets")
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;

    @GetMapping
    @ApiOperation(value = "Query wallet address")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "Current user id"),
            @ApiImplicitParam(name = "current", value = "Current user"),
            @ApiImplicitParam(name = "size", value = "Page size")
    })
    @PreAuthorize("hasAuthority('user_wallet_query')")
    public R<Page<UserWallet>> findByPage(@ApiIgnore Page<UserWallet> page, Long userId) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<UserWallet> userWalletPage = userWalletService.findByPage(page, userId);
        return R.ok(userWalletPage);
    }

    @GetMapping("/getCoinAddress/{coinId}")
    @ApiOperation(value = "Query wallet address by coin id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId" ,value = "Coin id")
    })
    public R<List<UserWallet>> getCoinAddress(@PathVariable("coinId") Long coinId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<UserWallet> userWallets = userWalletService.findUserWallets(userId, coinId);
        return R.ok(userWallets);
    }

    @PostMapping
    @ApiOperation(value = "Add a new wallet address")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userWallet" ,value = "userWallet's json")
    })
    public R save(@RequestBody @Validated UserWallet userWallet) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        userWallet.setUserId(userId);
        boolean save = userWalletService.save(userWallet);
        if (save) {
            return R.ok();
        }
        return R.fail("Add wallet address failed");
    }

    @PostMapping("/deleteAddress")
    @ApiOperation(value = "Delete wallet address")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "Address id"),
            @ApiImplicitParam(name = "payPassword", value = "Pay password")
    })
    public R delete(@RequestParam(required = true) Long addressId, @RequestParam(required = true) String payPassword) {
       boolean isOk = userWalletService.deleteUserWallet(addressId, payPassword);
       if(isOk) {
           return R.ok("Delete wallet address success");
       }
       return R.fail("Delete wallet address failed");
    }
}
