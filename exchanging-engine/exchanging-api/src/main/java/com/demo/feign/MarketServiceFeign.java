package com.demo.feign;

import com.demo.config.feign.OAuth2FeignConfig;
import com.demo.dto.MarketDto;
import com.demo.dto.TradeMarketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "exchange-service", contextId = "marketServiceFeign", configuration = OAuth2FeignConfig.class, path = "/markets")
public interface MarketServiceFeign {

    // Get Market by buy and sell coin id
    @GetMapping("/getMarket")
    MarketDto findByCoinId(@RequestParam("buyCoinId") Long buyCoinId, @RequestParam("sellCoinId") Long sellCoinId);

    // Get market by symbol
    @GetMapping("/getMarket/symbol")
    MarketDto findBySymbol(@RequestParam("symbol") String symbol);

    // Get all trade markets
    @GetMapping("/tradeMarkets")
    List<MarketDto> tradeMarkets();

    // Get the depth data by the symbol
    @GetMapping("/depthData/{symbol}/{type}")
    String depthData(@PathVariable("symbol") String symbol, @PathVariable("type") int value);

    // Get the trade market by the market id
    @GetMapping("/queryMarketsByIds")
    List<TradeMarketDto> queryMarketsByIds(@RequestParam("marketIds") String marketIds);

    // Query all trade data by the symbol
    @GetMapping("/trades")
    String trades(@RequestParam("symbol") String symbol);

    // Refresh 24 hours trade data
    @GetMapping(value = "/refresh_24hour")
    void refresh24hour(@RequestParam("symbol") String symbol);

}
