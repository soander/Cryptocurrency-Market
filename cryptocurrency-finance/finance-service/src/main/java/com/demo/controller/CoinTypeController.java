package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.CoinType;
import com.demo.model.R;
import com.demo.service.CoinTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/coinTypes")
@Api(value = "Coin Type Controller")
public class CoinTypeController {

    @Autowired
    private CoinTypeService coinTypeService;

    @GetMapping
    @ApiOperation(value = "Query Coin Type List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "Current Page"),
            @ApiImplicitParam(name = "size",value = "Page Size"),
            @ApiImplicitParam(name = "code",value = "Coin Type Code"),
    })
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    public R<Page<CoinType>> findByPage(@ApiIgnore Page<CoinType> page, String code) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<CoinType> coinTypePage = coinTypeService.findByPage(page,code);
        return R.ok(coinTypePage);
    }

    @PostMapping
    @ApiOperation(value = "Add Coin Type")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinType" ,value = "coinType's json")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_create')")
    public R add(@RequestBody @Validated CoinType coinType) {
        boolean save = coinTypeService.save(coinType);
        if(save) {
            return R.ok();
        }
        return R.fail("Add fail");
    }

    @PatchMapping
    @ApiOperation(value = "Update Coin Type")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinType" ,value = "coinType's json")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_update')")
    public R update(@RequestBody @Validated CoinType coinType) {
        boolean update = coinTypeService.updateById(coinType);
        if(update) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @PostMapping("/setStatus")
    @ApiOperation(value = "Set Coin Type Status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "coinType's Id") ,
            @ApiImplicitParam(name = "status" ,value = "Set the status of coin type")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_update')")
    public R setStatus(@RequestBody CoinType coinType) {
        boolean update = coinTypeService.updateById(coinType);
        if(update) {
            return R.ok();
        }
        return R.fail("Set status fail");
    }

    @GetMapping("/all")
    @ApiOperation(value = "Query All Coin Type")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status",value = "Coin Type Status")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    public R<List<CoinType>> findAllCoinTypeByStatus(Byte status) {
        List<CoinType> coinTypes = coinTypeService.listByStatus(status);
        return R.ok(coinTypes);
    }
}
