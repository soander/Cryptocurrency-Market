package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.CoinWithdrawAuditRecord;
import com.demo.mapper.CoinWithdrawAuditRecordMapper;
import com.demo.service.CoinWithdrawAuditRecordService;
import org.springframework.stereotype.Service;

@Service
public class CoinWithdrawAuditRecordServiceImpl extends ServiceImpl<CoinWithdrawAuditRecordMapper, CoinWithdrawAuditRecord> implements CoinWithdrawAuditRecordService {

}

