package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.constant.Constants;
import com.demo.domain.DepthItemVo;
import com.demo.domain.Market;
import com.demo.domain.TurnoverOrder;
import com.demo.dto.MarketDto;
import com.demo.dto.TradeMarketDto;
import com.demo.feign.MarketServiceFeign;
import com.demo.feign.OrderBooksFeignClient;
import com.demo.mappers.MarketDtoMappers;
import com.demo.model.R;
import com.demo.service.MarketService;
import com.demo.service.TurnoverOrderService;
import com.demo.vo.DepthItemVo;
import com.demo.vo.DepthsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/markets")
@Api(tags = "Market controller")
public class MarketController implements MarketServiceFeign {

    @Autowired
    private MarketService marketService;
    @Autowired
    private TurnoverOrderService turnoverOrderService;
    @Autowired
    private OrderBooksFeignClient orderBooksFeignClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    @ApiOperation(value = "Query markets by page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size")
    })
    @PreAuthorize("hasAuthority('trade_market_query')")
    public R<Page<Market>> findByPage(@ApiIgnore Page<Market> page, Long tradeAreaId, Byte status) {
        Page<Market> pageData = marketService.findByPage(page, tradeAreaId, status);
        return R.ok(pageData);
    }

    @PostMapping("/setStatus")
    @ApiOperation(value = "Open or close market status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "market", value = "market's json")
    })
    @PreAuthorize("hasAuthority('trade_market_update')")
    public R setStatus(@RequestBody Market market) {
        boolean updateById = marketService.updateById(market);
        if (updateById) {
            return R.ok();
        }
        return R.fail("Set status failed");
    }

    @PostMapping
    @ApiOperation(value = "Add a new market")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "market", value = "market's json")
    })
    @PreAuthorize("hasAuthority('trade_market_create')")
    public R save(@RequestBody Market market) {
        boolean save = marketService.save(market);
        if (save) {
            return R.ok();
        }
        return R.fail("Add market failed");
    }

    @PatchMapping
    @ApiOperation(value = "Update a market")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "market", value = "market's json")
    })
    @PreAuthorize("hasAuthority('trade_market_update')")
    public R update(@RequestBody Market market) {
        boolean updateById = marketService.updateById(market);
        if (updateById) {
            return R.ok();
        }
        return R.fail("Update failed");
    }

    @GetMapping("/all")
    @ApiOperation(value = "Query all markets")
    public R<List<Market>> listMarks() {
        return R.ok(marketService.list());
    }

    @ApiOperation(value = "Query market depth by symbol and depth type")
    @GetMapping("/depth/{symbol}/{dept}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "Symbol"),
            @ApiImplicitParam(name = "dept", value = "Depth type")
    })
    public R<DepthsVo> findDeptVosSymbol(@PathVariable("symbol") String symbol, String dept) {
        Market market = marketService.getMarkerBySymbol(symbol);

        DepthsVo depthsVo = new DepthsVo();
        depthsVo.setCnyPrice(market.getOpenPrice());
        depthsVo.setPrice(market.getOpenPrice());
        Map<String, List<DepthItemVo>> depthMap = orderBooksFeignClient.querySymbolDepth(symbol);
        if (!CollectionUtils.isEmpty(depthMap)) {
            depthsVo.setAsks(depthMap.get("asks"));
            depthsVo.setBids(depthMap.get("bids"));
        }
        return R.ok(depthsVo);
    }

    @ApiOperation(value = "Query turnover order by symbol")
    @GetMapping("/trades/{symbol}")
    public R<List<TurnoverOrder>> findSymbolTurnoverOrder(@PathVariable("symbol") String symbol) {
        List<TurnoverOrder> turnoverOrders = turnoverOrderService.findBySymbol(symbol);
        return R.ok(turnoverOrders);
    }


    /**
     * K 线的查询
     *
     * @param symbol 交易对
     * @param type   K 线类型
     * @return
     */
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "symbol" ,value = "交易对"),
            @ApiImplicitParam(name = "type" ,value = "k线类型")}
    )
    @GetMapping("/kline/{symbol}/{type}")
    public R<List<JSONArray>> queryKLine(@PathVariable("symbol") String symbol, @PathVariable("type") String type) {
        // 我们的K 线放在Redis 里面
        String redisKey = new StringBuilder(Constants.REDIS_KEY_TRADE_KLINE).append(symbol.toLowerCase()).append(":").append(type).toString();
        List<String> klines = redisTemplate.opsForList().range(redisKey, 0, Constants.REDIS_MAX_CACHE_KLINE_SIZE - 1);
        List<JSONArray> result =  new ArrayList<>(klines.size()) ;

        if (!CollectionUtils.isEmpty(klines)) {
            for (String kline : klines) {
                // 先把字符串转化为json的数组
                JSONArray objects = JSON.parseArray(kline);
                // 这样前端获取到的就是数字类型
                result.add(objects) ;
            }
            return R.ok(result);
        }

        return null;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get Market by buy and sell coin id
    * @Date 2022/6/5 15:15
    **/
    @Override
    public MarketDto findByCoinId(Long buyCoinId, Long sellCoinId) {
        MarketDto marketDto = marketService.findByCoinId(buyCoinId, sellCoinId);
        return marketDto;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get market by symbol
    * @Date 2022/6/5 15:15
    **/
    @Override
    public MarketDto findBySymbol(String symbol) {
        Market markerBySymbol = marketService.getMarkerBySymbol(symbol);
        return MarketDtoMappers.INSTANCE.toConvertDto(markerBySymbol);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get all trade markets
    * @Date 2022/6/5 15:15
    **/
    @Override
    public List<MarketDto> tradeMarkets() {
        return marketService.queryAllMarkets();
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get the depth data by the symbol
    * @Date 2022/6/5 15:16
    **/
    @Override
    public String depthData(String symbol, int value) {
        R<DepthsVo> deptVosSymbol = findDeptVosSymbol(symbol, value + "");
        DepthsVo data = deptVosSymbol.getData();
        return JSON.toJSONString(data);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get the trade market by the market id
    * @Date 2022/6/5 15:16
    **/
    @Override
    public List<TradeMarketDto> queryMarketsByIds(String marketIds) {
        return null;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query all trade data by the symbol
    * @Date 2022/6/5 15:16
    **/
    @Override
    public String trades(String symbol) {
        return null;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Refresh 24 hours trade data
    * @Date 2022/6/5 15:16
    **/
    @Override
    public void refresh24hour(String symbol) {

    }

}
