package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.WorkIssue;
import com.demo.model.R;
import com.demo.service.WorkIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/workIssues")
@Api(tags = "Work issues controller")
public class WorkIssueController {

    @Autowired
    private WorkIssueService workIssueService;

    @GetMapping
    @ApiOperation(value = "Query work issues by page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "Work issues' status"),
            @ApiImplicitParam(name = "startTime", value = "Work issues' start time"),
            @ApiImplicitParam(name = "endTime", value = "Work issues' end time"),
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Current number"),
    })
    @PreAuthorize("hasAuthority('work_issue_query')")
    public R<Page<WorkIssue>> findByPage(@ApiIgnore Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<WorkIssue> workIssuePage = workIssueService.findByPage(page, status, startTime, endTime);
        return R.ok(workIssuePage);
    }

    @PatchMapping
    @ApiOperation(value = "Answer a work issue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Work issue id"),
            @ApiImplicitParam(name = "answer", value = "Work issue's answer"),
    })
    @PreAuthorize("hasAuthority('work_issue_update')")
    public R work_issue_update(Long id ,String answer) {
        WorkIssue workIssue = new WorkIssue();
        workIssue.setId(id);
        workIssue.setAnswer(answer);
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        workIssue.setAnswerUserId(Long.valueOf(userIdStr));
        boolean updateById = workIssueService.updateById(workIssue);
        if(updateById) {
            return R.ok();
        }
        return R.fail("Answer work issue fail");
    }

    @GetMapping("/issueList")
    @ApiOperation(value = "前台查询工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" ,value = "当前页"),
            @ApiImplicitParam(name = "size" ,value = "显示的条数")
    })
    public R<Page<WorkIssue>> getIssueList(@ApiIgnore Page<WorkIssue> page) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Page<WorkIssue> pageData = workIssueService.getIssueList(page,Long.valueOf(userIdStr));
        return R.ok(pageData);
    }

    @PostMapping("/addWorkIssue")
    @ApiOperation(value = "会员添加问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workIssue" , value = "workIssue的json,只包含questions")
    })
    public R addWorkIssue(@RequestBody  WorkIssue workIssue) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        workIssue.setUserId(Long.valueOf(userIdStr));
        workIssue.setStatus(1);
        boolean save = workIssueService.save(workIssue);
        if(save) {
            return R.ok("Add success");
        }
        return R.fail("Add fail");
    }
}
