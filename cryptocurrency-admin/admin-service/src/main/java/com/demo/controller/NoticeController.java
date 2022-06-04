package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.Notice;
import com.demo.model.R;
import com.demo.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@RestController
@RequestMapping("/notices")
@Api(tags = "Notice Controller")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    @ApiOperation(value = "Query notice by page")
    @PreAuthorize("hasAuthority('notice_query')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current Page"),
            @ApiImplicitParam(name = "size", value = "Page notice number"),
            @ApiImplicitParam(name = "title", value = "Notice title"),
            @ApiImplicitParam(name = "startTime", value = "Notice created time"),
            @ApiImplicitParam(name = "endTime", value = "Notice end time"),
    })
    public R<Page<Notice>> findByPage(@ApiIgnore Page<Notice> page, String title, String startTime, String endTime, Integer status) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return R.ok(noticeService.findByPage(page, title, startTime, endTime, status));
    }

    // Delete a notice
    @PostMapping("/delete")
    @ApiOperation(value = "Detele a notice")
    @PreAuthorize("hasAuthority('notice_delete')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "Id collections delete")
    })
    public R delete(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return R.fail("Delete a notice need a id");
        }
        boolean b = noticeService.removeByIds(Arrays.asList(ids));
        if (b) {
            return R.ok();
        }
        return R.fail("delete fail");
    }

    // Update a notice
    @PostMapping("/updateStatus")
    @ApiOperation(value = "Star / Forbid a notice")
    @PreAuthorize("hasAuthority('notice_update')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Notice id"),
            @ApiImplicitParam(name = "status", value = "Notice status")

    })
    public R updateStatus(Long id, Integer status) {
        Notice notice = new Notice();
        notice.setId(id);
        notice.setStatus(status);
        boolean b = noticeService.updateById(notice);
        if (b) {
            return R.ok("Update success");
        }
        return R.fail("Update fail");
    }

    // Add a notice
    @PostMapping
    @ApiOperation(value = "Add a notice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice" ,value = "notice's json information")
    })
    @PreAuthorize("hasAuthority('notice_create')")
    public R add(@RequestBody @Validated Notice notice) {
        notice.setStatus(1);
        boolean save = noticeService.save(notice);
        if(save){
            return R.ok();
        }
        return  R.fail("Add fail");
    }

    // Update a notice
    @PatchMapping
    @ApiOperation(value = "Update a notice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice" ,value = "notice's json information")
    })
    @PreAuthorize("hasAuthority('notice_update')")
    public R update(@RequestBody  @Validated  Notice notice) {
        boolean update = noticeService.updateById(notice);
        if(update) {
            return R.ok();
        }
        return  R.fail("Update fail");
    }

    // Show notices
    @GetMapping("/simple")
    @ApiOperation(value = "Find notice simple")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "current page"),
            @ApiImplicitParam(name = "size",value = "current page number")
    })
    public R<Page<Notice>> findNoticeForSimple(Page<Notice> page) {
        Page<Notice> pageData = noticeService.findNoticeForSimple(page);
        return R.ok(pageData);
    }

    // Simple notice information
    @GetMapping("/simple/{id}")
    @ApiOperation(value = "Query notice detail by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "notice's id")
    })
    public R<Notice> noticeSimpleInfo(@PathVariable("id")Long id) {
        Notice notice = noticeService.getById(id);
        return R.ok(notice);
    }
}
