package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mappers.TradeAreaDtoMappers;
import com.demo.service.MarketService;
import com.demo.service.TradeAreaService;
import com.demo.domain.Market;
import com.demo.domain.TradeArea;
import com.demo.domain.UserFavoriteMarket;
import com.demo.dto.CoinDto;
import com.demo.dto.TradeAreaDto;
import com.demo.feign.CoinServiceFeign;
import com.demo.mapper.TradeAreaMapper;
import com.demo.vo.MergeDeptVo;
import com.demo.vo.TradeAreaMarketVo;
import com.demo.vo.TradeMarketVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeAreaServiceImpl extends ServiceImpl<TradeAreaMapper, TradeArea> implements TradeAreaService {

    @Autowired
    private MarketService marketService;
    @Autowired
    private CoinServiceFeign coinServiceFeign;
    @Autowired
    private UserFavoriteMarketService userFavoriteMarketService;

    /**
    * @Author Yaozheng Wang
    * @Description Query trade area data by page
    * @Date 2022/6/4 14:09
    **/
    @Override
    public Page<TradeArea> findByPage(Page<TradeArea> page, String name, Byte status) {
        return page(page, new LambdaQueryWrapper<TradeArea>()
                .eq(status != null, TradeArea::getStatus, status)
                .like(!StringUtils.isEmpty(name), TradeArea::getName, name)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query trade area data by status
    * @Date 2022/6/4 14:18
    **/
    @Override
    public List<TradeArea> findAll(Byte status) {
        return list(new LambdaQueryWrapper<TradeArea>()
                .eq(status != null, TradeArea::getStatus, status));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query all trade area market
    * @Date 2022/6/4 14:20
    **/
    @Override
    public List<TradeAreaMarketVo> findTradeAreaMarket() {
        List<TradeArea> tradeAreas = list(new LambdaQueryWrapper<TradeArea>().eq(TradeArea::getStatus, 1).orderByAsc(TradeArea::getSort));
        if (CollectionUtils.isEmpty(tradeAreas)) {
            return Collections.emptyList();
        }
        List<TradeAreaMarketVo> tradeAreaMarketVos = new ArrayList<>();
        for (TradeArea tradeArea : tradeAreas) {
            List<Market> markets = marketService.getMarkersByTradeAreaId(tradeArea.getId());
            if (!CollectionUtils.isEmpty(markets)) {
                TradeAreaMarketVo tradeAreaMarketVo = new TradeAreaMarketVo();
                tradeAreaMarketVo.setAreaName(tradeArea.getName());
                tradeAreaMarketVo.setMarkets(markets2marketVos(markets));
                tradeAreaMarketVos.add(tradeAreaMarketVo);
            }
        }
        return tradeAreaMarketVos;
    }
    
    /**
    * @Author Yaozheng Wang
    * @Description Markets transfer to marketVos
    * @Date 2022/6/5 10:46
    **/
    private List<TradeMarketVo> markets2marketVos(List<Market> markets) {
        return markets.stream().map(market -> toConvertVo(market)).collect(Collectors.toList());
    }

    /**
    * @Author Yaozheng Wang
    * @Description Convert to MarketVo
    * @Date 2022/6/5 10:46
    **/
    private TradeMarketVo toConvertVo(Market market) {
        TradeMarketVo tradeMarketVo = new TradeMarketVo();
        tradeMarketVo.setImage(market.getImg()); // Set image
        tradeMarketVo.setName(market.getName()); // Set name
        tradeMarketVo.setSymbol(market.getSymbol()); // Set symbol
        tradeMarketVo.setHigh(market.getOpenPrice()); // Set high price
        tradeMarketVo.setLow(market.getOpenPrice()); // Set low price
        tradeMarketVo.setPrice(market.getOpenPrice()); // Set price
        tradeMarketVo.setCnyPrice(market.getOpenPrice()); // Set RMB price
        tradeMarketVo.setCnyPrice(market.getOpenPrice()); // Set RMB price
        tradeMarketVo.setPriceScale(market.getPriceScale()); // Set price scale

        @NotNull Long buyCoinId = market.getBuyCoinId();
        List<CoinDto> coins = coinServiceFeign.findCoins(Arrays.asList(buyCoinId));

        if (CollectionUtils.isEmpty(coins) || coins.size() > 1) {
            throw new IllegalArgumentException("Coin is not exist");
        }
        CoinDto coinDto = coins.get(0);

        tradeMarketVo.setPriceUnit(coinDto.getName()); // Set price unit
        tradeMarketVo.setTradeMin(market.getTradeMin()); // Set the minimum amount of the transaction
        tradeMarketVo.setTradeMax(market.getTradeMax()); // Set the maximum amount of the transaction
        tradeMarketVo.setNumMin(market.getNumMin()); // Set the minimum number of orders
        tradeMarketVo.setNumMax(market.getNumMax()); // Set the maximum number of orders
        tradeMarketVo.setSellFeeRate(market.getFeeSell()); // Set the sell fee rate
        tradeMarketVo.setBuyFeeRate(market.getFeeBuy()); // Set the buy fee rate
        tradeMarketVo.setNumScale(market.getNumScale()); // Set the number of decimal places of the price
        tradeMarketVo.setSort(market.getSort()); // Set sort
        tradeMarketVo.setVolume(BigDecimal.ZERO); // Set trade volume
        tradeMarketVo.setAmount(BigDecimal.ZERO); // Set trade amount
        tradeMarketVo.setChange(0.00); // Set change
        tradeMarketVo.setMergeDepth(getMergeDepths(market.getMergeDepth())); // Set the merge depth of the market
        return tradeMarketVo;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get the merge depth of the market
    * @Date 2022/6/5 10:59
    **/
    private List<MergeDeptVo> getMergeDepths(String mergeDepth) {
        String[] split = mergeDepth.split(",");
        if (split.length != 3) {
            throw new IllegalArgumentException("Merge depth is invalid");
        }

        MergeDeptVo minMergeDeptVo = new MergeDeptVo();
        minMergeDeptVo.setMergeType("MIN"); // The minimum merge depth
        minMergeDeptVo.setValue(getDeptValue(Integer.valueOf(split[0])));

        MergeDeptVo defaultMergeDeptVo = new MergeDeptVo();
        defaultMergeDeptVo.setMergeType("DEFAULT"); // The default merge depth
        defaultMergeDeptVo.setValue(getDeptValue(Integer.valueOf(split[1])));

        MergeDeptVo maxMergeDeptVo = new MergeDeptVo();
        maxMergeDeptVo.setMergeType("MAX"); // The maximum merge depth
        maxMergeDeptVo.setValue(getDeptValue(Integer.valueOf(split[2])));

        List<MergeDeptVo> mergeDeptVos = new ArrayList<>();
        mergeDeptVos.add(minMergeDeptVo);
        mergeDeptVos.add(defaultMergeDeptVo);
        mergeDeptVos.add(maxMergeDeptVo);

        return mergeDeptVos;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Set the merge depth of the market
    * @Date 2022/6/5 11:03
    **/
    private BigDecimal getDeptValue(Integer scale) {
        BigDecimal bigDecimal = new BigDecimal(Math.pow(10, scale)); // Math.pow(10, scale)
        return BigDecimal.ONE.divide(bigDecimal).setScale(scale, RoundingMode.HALF_UP) ; // 1/10^n
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query user's favorite markets
    * @Date 2022/6/5 11:05
    **/
    @Override
    public List<TradeAreaMarketVo> getUserFavoriteMarkets(Long userId) {
        List<UserFavoriteMarket> userFavoriteMarkets = userFavoriteMarketService.list(new LambdaQueryWrapper<UserFavoriteMarket>()
                .eq(UserFavoriteMarket::getUserId, userId));
        if(CollectionUtils.isEmpty(userFavoriteMarkets)){
            return Collections.emptyList() ;
        }
        List<Long> marketIds = userFavoriteMarkets.stream().map(UserFavoriteMarket::getMarketId).collect(Collectors.toList());

        TradeAreaMarketVo tradeAreaMarketVo = new TradeAreaMarketVo();
        tradeAreaMarketVo.setAreaName("Favorite markets");
        List<Market> markets = marketService.listByIds(marketIds);
        List<TradeMarketVo> tradeMarketVos = markets2marketVos(markets);
        tradeAreaMarketVo.setMarkets(tradeMarketVos);
        return Arrays.asList(tradeAreaMarketVo);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query all trade area market
    * @Date 2022/6/5 11:07
    **/
    @Override
    public List<TradeAreaDto> findAllTradeAreaAndMarket() {
        List<TradeArea> tradeAreas = findAll((byte) 1);
        List<TradeAreaDto> tradeAreaDtoList = TradeAreaDtoMappers.INSTANCE.toConvertDto(tradeAreas);
        if(CollectionUtils.isEmpty(tradeAreaDtoList)) {
            for (TradeAreaDto tradeAreaDto : tradeAreaDtoList) {
                List<Market> markets = marketService.queryByAreaId(tradeAreaDto.getId());
                if(!CollectionUtils.isEmpty(markets)) {
                    String marketIds = markets.stream().map(market -> market.getId().toString()).collect(Collectors.joining(","));
                    tradeAreaDto.setMarketIds(marketIds);
                }
            }
        }
        return tradeAreaDtoList;
    }
}
