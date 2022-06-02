package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.CashWithdrawAuditRecord;
import com.demo.domain.CashWithdrawals;
import com.demo.model.CashSellParam;

public interface CashWithdrawalsService extends IService<CashWithdrawals> {

    // Query cash withdrawals records by page
    Page<CashWithdrawals> findByPage(Page<CashWithdrawals> page, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    // Query user's cash withdrawals records by page
    Page<CashWithdrawals> findCashWithdrawals(Page<CashWithdrawals> page, Long userId, Byte status);

    // Sell GCN
    boolean sell(Long userId, CashSellParam cashSellParam);

    // Audit cash withdrawals status
    boolean updateWithdrawalsStatus(Long userId, CashWithdrawAuditRecord cashWithdrawAuditRecord);
}

