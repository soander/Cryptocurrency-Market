package com.demo.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.AccountDetail;
import com.demo.model.R;
import com.demo.service.AccountDetailService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/accountDetails")
public class AccountDetailController {

    @Autowired
    private AccountDetailService accountDetailService;

    // Query account detail records by page
    @GetMapping("/records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "accountId", value = "Account id"),
            @ApiImplicitParam(name = "coinId", value = "Coin id"),
            @ApiImplicitParam(name = "userId", value = "User id"),
            @ApiImplicitParam(name = "userName", value = "User name"),
            @ApiImplicitParam(name = "mobile", value = "Phone number"),
            @ApiImplicitParam(name = "amountStart", value = "Start amount"),
            @ApiImplicitParam(name = "amountEnd", value = "End amount"),
            @ApiImplicitParam(name = "startTime", value = "Start time"),
            @ApiImplicitParam(name = "endTime", value = "End time"),
    })
    public R<Page<AccountDetail>> findByPage(@ApiIgnore Page<AccountDetail> page, String amountStart,
                                             String amountEnd, Long userId, String userName, String mobile,
                                             Long coinId, Long accountId, String startTime, String endTime) {
        Page<AccountDetail> pageData = accountDetailService.findByPage(page, coinId, accountId, userId,
                userName, mobile, amountStart, amountEnd, startTime, endTime);
        return R.ok(pageData);
    }

    @GetMapping("/export")
    public void exportAccountDetailRecords(@ApiIgnore HttpServletResponse response, String amountStart,
                                           String amountEnd, Long userId, String userName, String mobile,
                                            Long coinId, Long accountId, String startTime, String endTime) throws IOException {
        Page<AccountDetail> accountDetailPage = new Page<AccountDetail>(1, 100000);
        Page<AccountDetail> pageData
                = accountDetailService.findByPage(accountDetailPage, coinId, accountId, userId, userName, mobile, amountStart, amountEnd, startTime, endTime);
        List<AccountDetail> records = pageData.getRecords();
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.write(records) ;
        response.setContentType("application/vnd.ms-excle;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=test.xls");
        writer.flush(response.getOutputStream()) ;
    }
}
