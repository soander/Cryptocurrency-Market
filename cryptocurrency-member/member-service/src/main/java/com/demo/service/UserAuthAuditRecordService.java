package com.demo.service;

import com.demo.domain.UserAuthAuditRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserAuthAuditRecordService extends IService<UserAuthAuditRecord>{

    /**
     * 获取一个用户的审核记录
     * @param id
     * @return
     */
    List<UserAuthAuditRecord> getUserAuthAuditRecordList(Long id);
}
