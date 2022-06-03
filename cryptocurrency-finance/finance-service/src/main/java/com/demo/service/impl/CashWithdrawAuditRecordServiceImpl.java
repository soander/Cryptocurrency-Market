package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.CashWithdrawAuditRecord;
import com.demo.mapper.CashWithdrawAuditRecordMapper;
import com.demo.service.CashWithdrawAuditRecordService;
import org.springframework.stereotype.Service;

@Service
public class CashWithdrawAuditRecordServiceImpl extends ServiceImpl<CashWithdrawAuditRecordMapper, CashWithdrawAuditRecord> implements CashWithdrawAuditRecordService {

}

