package com.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mappers.MarketDtoMappers;
import com.demo.service.MarketService;
import com.demo.service.TurnoverOrderService;
import com.demo.domain.Market;
import com.demo.dto.CoinDto;
import com.demo.dto.MarketDto;
import com.demo.feign.CoinServiceFeign;
import com.demo.mapper.MarketMapper;
import com.demo.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService {

    @Autowired
    private CoinServiceFeign coinServiceFeign;
    @Autowired
    private TurnoverOrderService turnoverOrderService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ConfigService configService;

    /**
    * @Author Yaozheng Wang
    * @Description Query markets by page
    * @Date 2022/6/4 14:22
    **/
    @Override
    public Page<Market> findByPage(Page<Market> page, Long tradeAreaId, Byte status) {
        return page(page, new LambdaQueryWrapper<Market>()
                .eq(tradeAreaId != null, Market::getTradeAreaId, tradeAreaId)
                .eq(status != null, Market::getStatus, status)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Add a new market
    * @Date 2022/6/4 14:40
    **/
    @Override
    public boolean save(Market entity) {
        log.info("Add new market{}", JSON.toJSONString(entity));
        @NotBlank Long sellCoinId = entity.getSellCoinId();
        @NotNull Long buyCoinId = entity.getBuyCoinId();
        List<CoinDto> coins = coinServiceFeign.findCoins(Arrays.asList(sellCoinId, buyCoinId));
        if (CollectionUtils.isEmpty(coins) || coins.size() != 2) {
            throw new IllegalArgumentException("Coin not found");
        }
        CoinDto coinDto = coins.get(0);
        CoinDto sellCoin;
        CoinDto buyCoin;
        if (coinDto.getId().equals(sellCoinId)) {
            sellCoin = coinDto;
            buyCoin = coins.get(1);
        } else {
            sellCoin = coins.get(1);
            buyCoin = coinDto;
        }

        entity.setName(sellCoin.getName() + "/" + buyCoin.getName());
        entity.setTitle(sellCoin.getTitle() + "/" + buyCoin.getTitle());
        entity.setSymbol(sellCoin.getName() + buyCoin.getName());
        entity.setImg(sellCoin.getImg());

        return super.save(entity);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query markets by trade area id
    * @Date 2022/6/5 10:48
    **/
    @Override
    public List<Market> getMarkersByTradeAreaId(Long id) {
        return list(new LambdaQueryWrapper<Market>()
                .eq(Market::getTradeAreaId, id)
                .eq(Market::getStatus, 1)
                .orderByAsc(Market::getSort)
        );
    }

    // Query market by symbol
    @Override
    public Market getMarkerBySymbol(String symbol) {
        return getOne(new LambdaQueryWrapper<Market>().eq(Market::getSymbol, symbol));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get Market by buy and sell coin id
    * @Date 2022/6/5 15:21
    **/
    @Override
    public MarketDto findByCoinId(Long buyCoinId, Long sellCoinId) {
        LambdaQueryWrapper<Market> eq = new LambdaQueryWrapper<Market>()
                .eq(Market::getBuyCoinId, buyCoinId)
                .eq(Market::getSellCoinId, sellCoinId)
                .eq(Market::getStatus, 1);
        Market one = getOne(eq);
        if (one == null) {
            return null;
        }
        MarketDto marketDto = MarketDtoMappers.INSTANCE.toConvertDto(one);
        return marketDto;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query all markets
    * @Date 2022/6/5 15:21
    **/
    @Override
    public List<MarketDto> queryAllMarkets() {
        List<Market> list = list(new LambdaQueryWrapper<Market>().eq(Market::getStatus, 1));
        return MarketDtoMappers.INSTANCE.toConvertDto(list);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query markets by trade area id
    * @Date 2022/6/5 15:21
    **/
    @Override
    public List<Market> queryByAreaId(Long id) {
        return null;
    }
}
