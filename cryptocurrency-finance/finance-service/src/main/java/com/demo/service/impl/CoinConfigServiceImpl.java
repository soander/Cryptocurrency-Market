package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.Coin;
import com.demo.domain.CoinConfig;
import com.demo.mapper.CoinConfigMapper;
import com.demo.service.CoinConfigService;
import com.demo.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinConfigServiceImpl extends ServiceImpl<CoinConfigMapper, CoinConfig> implements CoinConfigService {

    @Autowired
    private CoinService coinService;

    /**
    * @Author Yaozheng Wang
    * @Description Query coin config information by coin id
    * @Date 2022/6/1 8:51
    * @Param Coin id
    * @Return coin config
    **/
    @Override
    public CoinConfig findByCoinId(Long coinId) {
        return getOne(new LambdaQueryWrapper<CoinConfig>().eq(CoinConfig::getId, coinId));
    }

    // Update or save coin config
    @Override
    public boolean updateOrSave(CoinConfig coinConfig) {
        Coin coin = coinService.getById(coinConfig.getId());
        if(coin == null) {
            throw new IllegalArgumentException("coin id is not exist");
        }
        coinConfig.setCoinType(coin.getType());
        coinConfig.setName(coin.getName());

        CoinConfig config = getById(coinConfig.getId());
        if (config == null) { // Save
            return save(coinConfig);
        } else { // Update
            return updateById(coinConfig);
        }
    }
}

