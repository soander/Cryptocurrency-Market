package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.AdminAddress;
import com.demo.domain.Coin;
import com.demo.mapper.AdminAddressMapper;
import com.demo.service.AdminAddressService;
import com.demo.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAddressServiceImpl extends ServiceImpl<AdminAddressMapper, AdminAddress> implements AdminAddressService{

    @Autowired
    private CoinService coinService;

    /**
    * @Author Yaozheng Wang
    * @Description Query all admin address by page and coin id
    * @Date 2022/6/1 8:57
    * @Param Page and coin id
    * @Return Admin address page
    **/
    @Override
    public Page<AdminAddress> findByPage(Page<AdminAddress> page, Long coinId) {
        return page(page,new LambdaQueryWrapper<AdminAddress>().eq(coinId!=null ,AdminAddress::getCoinId,coinId));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Save admin address
    * @Date 2022/6/1 9:08
    * @Param admin address
    * @Return boolean
    **/
    @Override
    public boolean save(AdminAddress entity) {
        Long coinId = entity.getCoinId();
        Coin coin = coinService.getById(coinId);
        if(coin == null) {
            throw new IllegalArgumentException("The coin id is not exist");
        }
        String type = coin.getType();
        entity.setCoinType(type);
        return super.save(entity);
    }
}
