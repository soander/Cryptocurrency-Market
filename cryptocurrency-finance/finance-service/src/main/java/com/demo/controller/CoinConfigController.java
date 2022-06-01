package com.demo.controller;

import com.demo.domain.CoinConfig;
import com.demo.model.R;
import com.demo.service.CoinConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coinConfigs")
@Api(tags = "Coin config controller")
public class CoinConfigController {

    @Autowired
    private CoinConfigService coinConfigService;

    @GetMapping("/info/{coinId}")
    @ApiOperation(value = "Query coin config info by coin id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId" ,value = "Coin id")
    })
    public R<CoinConfig> getCoinConfig(@PathVariable("coinId") Long coinId) {
        CoinConfig coinConfig = coinConfigService.findByCoinId(coinId);
        return R.ok(coinConfig);
    }

    @PatchMapping
    @ApiOperation(value = "Update coin config info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinConfig" ,value ="coinConfig's json")
    })
    public R update(@RequestBody @Validated CoinConfig coinConfig) {
       boolean saveOrUpdate = coinConfigService.updateOrSave(coinConfig);
        if(saveOrUpdate) {
            return R.ok();
        }
        return R.fail("Update fail");
    }
}
