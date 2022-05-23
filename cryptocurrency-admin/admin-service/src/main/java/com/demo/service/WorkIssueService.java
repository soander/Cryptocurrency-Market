package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.WorkIssue;
import com.baomidou.mybatisplus.extension.service.IService;
public interface WorkIssueService extends IService<WorkIssue> {


    /**
     * find By Page
     * @param page
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime);

    /**
     * get Issue List
     * @param page
     * @param  userId
     * @return
     */
    Page<WorkIssue> getIssueList(Page<WorkIssue> page,Long userId);
}
