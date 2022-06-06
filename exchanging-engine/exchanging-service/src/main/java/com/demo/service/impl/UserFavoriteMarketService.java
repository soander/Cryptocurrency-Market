package com.demo.service.impl;

import com.demo.domain.UserFavoriteMarket;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserFavoriteMarketService extends IService<UserFavoriteMarket>{

    // Delete user's favorite market
    boolean deleteUserFavoriteMarket(Long marketId, Long userId);
}
