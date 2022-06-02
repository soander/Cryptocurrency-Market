package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.AccountDetail;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AccountDetailService extends IService<AccountDetail> {

    // Query account detail records by page
    Page<AccountDetail> findByPage(Page<AccountDetail> page, Long coinId, Long accountId, Long userId, String userName, String mobile, String amountStart, String amountEnd, String startTime, String endTime);
}

