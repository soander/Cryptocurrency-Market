package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.CoinWithdraw;
import com.demo.model.R;
import com.demo.service.CoinWithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/coinWithdraws")
@Api(tags = "Coin Withdraw Controller")
public class CoinWithdrawController {

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    @GetMapping("/records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "coinId", value = "Current page"),
            @ApiImplicitParam(name = "userId", value = "User id"),
            @ApiImplicitParam(name = "userName", value = "User name"),
            @ApiImplicitParam(name = "mobile", value = "Phone number"),
            @ApiImplicitParam(name = "status", value = "Status"),
            @ApiImplicitParam(name = "numMin", value = "Min number"),
            @ApiImplicitParam(name = "numMax", value = "Max number"),
            @ApiImplicitParam(name = "startTime", value = "Start time"),
            @ApiImplicitParam(name = "endTime", value = "End time"),
    })
    public R<Page<CoinWithdraw>> findByPage(@ApiIgnore Page<CoinWithdraw> page, Long coinId,
            Long userId, String userName, String mobile,
            Byte status, String numMin, String numMax,
            String startTime, String endTime) {
        Page<CoinWithdraw> pageData = coinWithdrawService.findByPage(page, coinId, userId, userName,
                mobile, status, numMin, numMax, startTime, endTime);
        return R.ok(pageData);
    }

    @GetMapping("/user/record")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" ,value = "Current page"),
            @ApiImplicitParam(name = "size" ,value = "Page size"),
            @ApiImplicitParam(name = "coinId" ,value = "Coin id"),
    })
    @ApiOperation(value = "Query user coin withdraw records")
    public R<Page<CoinWithdraw>> findUserCoinWithdraw(@ApiIgnore Page<CoinWithdraw> page, Long coinId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CoinWithdraw> coinWithdrawPage = coinWithdrawService.findUserCoinWithdraw(userId, coinId, page);
        return R.ok(coinWithdrawPage);
    }
}
