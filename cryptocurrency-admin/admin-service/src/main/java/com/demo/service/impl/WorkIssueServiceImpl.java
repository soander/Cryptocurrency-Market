package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.WorkIssue;
import com.demo.dto.UserDto;
import com.demo.feign.UserServiceFeign;
import com.demo.mapper.WorkIssueMapper;
import com.demo.service.WorkIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
    * @Author Yaozheng Wang
    * @Description Find work issues by page
    * @Date 2022/6/4 15:44
    **/
    @Override
    public Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        Page<WorkIssue> pageData = page(page, new LambdaQueryWrapper<WorkIssue>()
                .eq(status != null, WorkIssue::getStatus, status)
                .between(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime),
                        WorkIssue::getCreated,
                        startTime, endTime + " 23:59:59")
        );
        List<WorkIssue> records = pageData.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageData;
        }

        //1 Get user ids
        List<Long> userIds = records.stream().map(WorkIssue::getUserId).collect(Collectors.toList());
        // 2 Use feign to get user info
        Map<Long, UserDto> idMapUserDtos = userServiceFeign.getBasicUsers(userIds, null, null);
        records.forEach(workIssue -> {
            UserDto userDto = idMapUserDtos.get(workIssue.getUserId());
            workIssue.setUsername(userDto == null ? "Test User" : userDto.getUsername());
            workIssue.setRealName(userDto == null ? "Test User" : userDto.getRealName());
        });
        return pageData;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get work issue list
    * @Date 2022/6/4 15:44
    **/
    @Override
    public Page<WorkIssue> getIssueList(Page<WorkIssue> page,Long userId) {
        return page(page,new LambdaQueryWrapper<WorkIssue>()
                .eq(WorkIssue::getUserId,userId)
                .eq(WorkIssue::getStatus,1)
        );
    }
}
