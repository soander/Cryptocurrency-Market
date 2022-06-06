package com.demo.controller;

import cn.hutool.core.lang.Snowflake;
import com.demo.domain.Market;
import com.demo.domain.UserFavoriteMarket;
import com.demo.model.R;
import com.demo.service.MarketService;
import com.demo.service.impl.UserFavoriteMarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/userFavorites")
@Api(tags = "User favorite market controller")
public class UserFavoriteMarketController {

    @Autowired
    private UserFavoriteMarketService userFavoriteMarketService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private Snowflake snowflake;

    @PostMapping("/addFavorite")
    @ApiOperation(value = "Add user favorite market")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "market", value = "market's symbol and name")
    })
    public R addFavorite(@RequestBody Market market) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()) ;
        UserFavoriteMarket userFavoriteMarket = new UserFavoriteMarket();
        @NotNull String symbol = market.getSymbol();
        Market marketDb = marketService.getMarkerBySymbol(symbol);

        userFavoriteMarket.setId(snowflake.nextId());
        userFavoriteMarket.setUserId(userId);
        userFavoriteMarket.setMarketId(marketDb.getId());
        userFavoriteMarket.setType(market.getType().intValue());
        boolean save = userFavoriteMarketService.save(userFavoriteMarket);
        if (save) {
            return R.ok("Add user favorite market success");
        }
        return R.fail("Add user favorite market failed");
    }

    @DeleteMapping("/{symbol}")
    @ApiOperation(value = "User delete favorite market")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol" ,value = "Symbol of market")
    })
    public R deleteUserFavoriteMarket(@PathVariable("symbol") String symbol) {
        Market markerBySymbol = marketService.getMarkerBySymbol(symbol);
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userFavoriteMarketService.deleteUserFavoriteMarket(markerBySymbol.getId(),userId);
        if(isOk) {
            return R.ok();
        }
       return R.fail("Delete user favorite market failed");
    }
}
