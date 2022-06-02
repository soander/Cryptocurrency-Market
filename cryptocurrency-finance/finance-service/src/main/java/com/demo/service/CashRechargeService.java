package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.CashRecharge;
import com.demo.domain.CashRechargeAuditRecord;
import com.demo.model.CashParam;
import com.demo.vo.CashTradeVo;

public interface CashRechargeService extends IService<CashRecharge> {

    // Query cash recharge records by page
    Page<CashRecharge> findByPage(Page<CashRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    // Query user cash recharge records by page
    Page<CashRecharge> findUserCashRecharge(Page<CashRecharge> page, Long userId, Byte status);

    // Buy GCN
    CashTradeVo buy(Long userId, CashParam cashParam);

    // Audit cash recharge
    boolean cashRechargeAudit(Long userId, CashRechargeAuditRecord cashRechargeAuditRecord);
}

