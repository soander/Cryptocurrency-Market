package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.CoinWithdraw;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CoinWithdrawService extends IService<CoinWithdraw> {

    // Query coin withdraw records by page
    Page<CoinWithdraw> findByPage(Page<CoinWithdraw> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    // Query user coin withdraw records by page
    Page<CoinWithdraw> findUserCoinWithdraw(Long userId, Long coinId, Page<CoinWithdraw> page);
}

