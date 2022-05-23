package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.WorkIssue;
//import com.demo.dto.UserDto;
//import com.demo.feign.UserServiceFeign;
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

//    @Autowired
//    private UserServiceFeign userServiceFeign;

    /**
     *
     * @param page
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
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
        // 远程调用member-service
        // 错误的示范
//        for (WorkIssue record : records) {
//            Long userId = record.getUserId();
//            // 使用userId->远程调用member-service
//        }
        //1 收集Id
        List<Long> userIds = records.stream().map(WorkIssue::getUserId).collect(Collectors.toList());
        // 2 远程调用
//        List<UserDto> basicUsers = userServiceFeign.getBasicUsers(userIds);
        // 2.1 小技巧: list->map<id,userDto>
//        if(CollectionUtils.isEmpty(basicUsers)){
//            return pageData ;
//        }
//
//        Map<Long, UserDto> idMapUserDtos = basicUsers.stream().
//                                collect(
//                                            Collectors.toMap(
//                                                    userDto -> userDto.getId(),  // key
//                                                    userDto -> userDto) //value
//                                );
//        Map<Long, UserDto> idMapUserDtos = userServiceFeign.getBasicUsers(userIds, null, null);
//        records.forEach(workIssue -> { // 循环每一个workIssue ,给它里面设置用户的信息 map.get(userId)
//            UserDto userDto = idMapUserDtos.get(workIssue.getUserId());
//            workIssue.setUsername(userDto == null ? "测试用户" : userDto.getUsername());
//            workIssue.setRealName(userDto == null ? "测试用户" : userDto.getRealName());
//        });
        return pageData;
    }


    /**
     * @param page
     * @return
     */
    @Override
    public Page<WorkIssue> getIssueList(Page<WorkIssue> page,Long userId) {
        return page(page,new LambdaQueryWrapper<WorkIssue>()
                                            .eq(WorkIssue::getUserId,userId)
//                                            .eq(WorkIssue::getStatus,1)
        );
    }
}
