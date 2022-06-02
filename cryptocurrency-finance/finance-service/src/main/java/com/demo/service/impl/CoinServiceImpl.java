package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.Coin;
import com.demo.dto.CoinDto;
import com.demo.mapper.CoinMapper;
import com.demo.mappers.CoinMappersDto;
import com.demo.service.CoinService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService {

    /**
    * @Author Yaozheng Wang
    * @Description Query coin list
    * @Date 2022/5/31 10:37
    * @param name: Coin name
    * @param type: Coin type
    * @param status: Coin status
    * @param title: Coin title
    * @param walletType: Coin wallet type
    * @param page: Page
    * @Return * coin list page
    **/
    @Override
    public Page<Coin> findByPage(String name, String type, Byte status, String title, String walletType, Page<Coin> page) {
        return page(page, new LambdaQueryWrapper<Coin>()
                        .like(!StringUtils.isEmpty(name), Coin::getName, name)
                        .like(!StringUtils.isEmpty(title), Coin::getTitle, title)
                        .eq(status != null, Coin::getStatus, status)
                        .eq(!StringUtils.isEmpty(type), Coin::getType, type)
                        .eq(!StringUtils.isEmpty(walletType), Coin::getWallet, walletType)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query all coin by status
    * @Date 2022/6/1 8:54
    * @Param
    * @Return * @return null
    **/
    @Override
    public List<Coin> getCoinsByStatus(Byte status) {
        return list(new LambdaQueryWrapper<Coin>().eq(Coin::getStatus,status));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query coin by coin name
    * @Date 2022/6/2 11:00
    * @Param coin name
    * @Return coin
    **/
    @Override
    public Coin getCoinByCoinName(String coinName) {
        return getOne(new LambdaQueryWrapper<Coin>().eq(Coin::getName,coinName));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query coin list by coin ids
    * @Date 2022/6/2 11:01
    * @Param coin ids list
    * @Return coin dto list
    **/
    @Override
    public List<CoinDto> findList(List<Long> coinIds) {
        List<Coin> coins = super.listByIds(coinIds);
        if(CollectionUtils.isEmpty(coinIds)){
            return Collections.emptyList() ;
        }
        List<CoinDto> coinDtos = CoinMappersDto.INSTANCE.toConvertDto(coins);
        return coinDtos;
    }
}

