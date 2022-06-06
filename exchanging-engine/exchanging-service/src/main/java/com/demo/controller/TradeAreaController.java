package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.TradeArea;
import com.demo.dto.TradeAreaDto;
import com.demo.feign.TradingAreaServiceClient;
import com.demo.model.R;
import com.demo.service.TradeAreaService;
import com.demo.vo.TradeAreaMarketVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/tradeAreas")
@Api(tags = "Trade area data api")
public class TradeAreaController implements TradingAreaServiceClient {

    @Autowired
    private TradeAreaService tradeAreaService;

    @GetMapping
    @ApiOperation(value = "Query trade area data by page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "name", value = "Trade area name"),
            @ApiImplicitParam(name = "status", value = "Trade area status"),
    })
    @PreAuthorize("hasAuthority('trade_area_query')")
    public R<Page<TradeArea>> findByPage(@ApiIgnore Page<TradeArea> page, String name, Byte status) {
        Page<TradeArea> pageData = tradeAreaService.findByPage(page, name, status);
        return R.ok(pageData);
    }

    @PostMapping
    @ApiOperation(value = "Add trade area data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradeArea", value = "Trade area's json")
    })
    @PreAuthorize("hasAuthority('trade_area_create')")
    public R save(@RequestBody TradeArea tradeArea) {
        boolean save = tradeAreaService.save(tradeArea);
        if (save) {
            return R.ok();
        }
        return R.fail("Add trade area data failed");
    }

    @PatchMapping
    @ApiOperation(value = "Update a trade area data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradeArea", value = "Trade area's json")
    })
    @PreAuthorize("hasAuthority('trade_area_update')")
    public R update(@RequestBody TradeArea tradeArea) {
        boolean update = tradeAreaService.updateById(tradeArea);
        if (update) {
            return R.ok();
        }
        return R.fail("Update trade area data failed");
    }

    @PostMapping("/status")
    @ApiOperation(value = "Update trade area status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradeArea", value = "Trade area's json")
    })
    @PreAuthorize("hasAuthority('trade_area_update')")
    public R updateStatus(@RequestBody TradeArea tradeArea) {
        boolean update = tradeAreaService.updateById(tradeArea);
        if (update) {
            return R.ok();
        }
        return R.fail("Update trade area status failed");
    }

    @GetMapping("/all")
    @ApiOperation(value = "Query all trade area data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "Trade area status")
    })
    @PreAuthorize("hasAuthority('trade_area_query')")
    public R<List<TradeArea>> findAll(Byte status) {
        List<TradeArea> tradeAreas = tradeAreaService.findAll(status);
        return R.ok(tradeAreas);
    }

    @GetMapping("/markets")
    @ApiOperation(value = "Query all trade area market")
    public R<List<TradeAreaMarketVo>> getTradeAreaMarkets(){
        List<TradeAreaMarketVo> tradeAreaMarketVos = tradeAreaService.findTradeAreaMarket();
        return R.ok(tradeAreaMarketVos);
    }

    @GetMapping("/market/favorite")
    @ApiOperation(value = "Query user's favorite trade area market")
    public R<List<TradeAreaMarketVo>> getUserFavoriteMarkets(){
       Long userId =  Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<TradeAreaMarketVo> tradeAreaMarketVos =  tradeAreaService.getUserFavoriteMarkets(userId) ;
        return R.ok(tradeAreaMarketVos) ;
    }

    @Override
    public List<TradeAreaDto> tradeAreas() {
        List<TradeAreaDto> tradeAreaDtoList =  tradeAreaService.findAllTradeAreaAndMarket();
        return tradeAreaDtoList ;
    }
}
