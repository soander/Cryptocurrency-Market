package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.Coin;
import com.demo.dto.CoinDto;
import com.demo.feign.CoinServiceFeign;
import com.demo.model.R;
import com.demo.service.CoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/coins")
@Api(tags = "Cryptocurrency data api")
public class CoinController implements CoinServiceFeign {

    @Autowired
    private CoinService coinService;

    @GetMapping
    @ApiOperation(value = "Query coin list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name" ,value = "Coin name"),
            @ApiImplicitParam(name = "type" ,value = "Coin type name"),
            @ApiImplicitParam(name = "status" ,value = "Coin status"),
            @ApiImplicitParam(name = "status" ,value = "Coin status title") ,
            @ApiImplicitParam(name = "wallet_type" ,value = "Coin wallet type"),
            @ApiImplicitParam(name = "current" ,value = "Current page"),
            @ApiImplicitParam(name = "size" ,value = "Page size")})
    public R<Page<Coin>> findByPage(String name, String type, Byte status, String title,
                                    @RequestParam(name = "wallet_type",required = false) String walletType,
                                    @ApiIgnore Page<Coin> page) {
       Page<Coin> coinPage = coinService.findByPage(name, type, status, title, walletType, page);
       return R.ok(coinPage) ;
    }

    @PostMapping("/setStatus")
    @ApiOperation(value = "Open or close coin status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin" ,value = "coin's json")
    })
    public R setStatus(@RequestBody Coin coin) {
        boolean updateById = coinService.updateById(coin);
        if(updateById) {
            return R.ok();
        }
        return R.fail("Set fail");

    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "Query coin info by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "Coin's id")
    })
    public R<Coin> info(@PathVariable("id") Long id) {
        Coin coin = coinService.getById(id);
        return R.ok(coin);
    }

    @GetMapping("/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status",value = "Coin current status")
    })
    @ApiOperation(value = "Query all coin list by status")
    public R<List<Coin>> getCoinAll(Byte status) {
       List<Coin> coins = coinService.getCoinsByStatus(status);
       return R.ok(coins);
    }

    @PatchMapping
    @ApiOperation(value = "Update coin info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin" ,value = "coin's json")
    })
    public R update(@RequestBody @Validated Coin coin) {
        boolean updateById = coinService.updateById(coin);
        if(updateById) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @PostMapping
    @ApiOperation(value = "Add coin info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin" ,value = "coin's json")
    })
    public R<Coin> save(@RequestBody @Validated Coin coin) {
        coin.setStatus((byte)1);
        boolean save = coinService.save(coin);
        if(save) {
            return R.ok(coin);
        }
        return R.fail("Add fail");
    }

    @Override
    public List<CoinDto> findCoins(List<Long> coinIds) {
        List<CoinDto> coinDtos =  coinService.findList(coinIds) ;
        return coinDtos;
    }
}
