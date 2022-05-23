package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.Notice;
import com.demo.mapper.NoticeMapper;
import com.demo.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

    /**
     * check notice
     *
     * @param page
     * @param title
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    @Override
    public Page<Notice> findByPage(Page<Notice> page, String title, String startTime, String endTime, Integer status) {
        return page(page, new LambdaQueryWrapper<Notice>()
            .like(!StringUtils.isEmpty(title), Notice::getTitle,title)
            .between(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime), Notice::getCreated,startTime,endTime+" 23:59:59")
            .eq(status!=null, Notice::getStatus, status)
        );
    }

    /**
     * @param page
     * @return
     */
    @Override
    public Page<Notice> findNoticeForSimple(Page<Notice> page) {
        return page(page,new LambdaQueryWrapper<Notice>()
            .eq(Notice::getStatus,1)
            .orderByAsc(Notice::getSort)
        );
    }
}
