package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.CoinRecharge;
import com.demo.model.R;
import com.demo.service.CoinRechargeService;
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
@RequestMapping("/coinRecharges")
@Api(tags = "Coin Recharge Controller")
public class CoinRechargeController {

    @Autowired
    private CoinRechargeService coinRechargeService;

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
    public R<Page<CoinRecharge>> findByPage(@ApiIgnore Page<CoinRecharge> page, Long coinId,
                                            Long userId, String userName, String mobile,
                                            Byte status, String numMin, String numMax,
                                            String startTime, String endTime) {
        Page<CoinRecharge> pageData = coinRechargeService.findByPage(page, coinId,
                userId, userName, mobile, status, numMin, numMax, startTime, endTime);
        return R.ok(pageData);
    }

    @GetMapping("/user/record")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "coinId", value = "Coin id"),
    })
    @ApiOperation(value = "Query user recharge records")
    public R<Page<CoinRecharge>> findUserCoinRecharge(@ApiIgnore Page<CoinRecharge> page ,Long coinId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CoinRecharge> pageData = coinRechargeService.findUserCoinRecharge(page, coinId, userId);
        return R.ok(pageData);
    }
}
