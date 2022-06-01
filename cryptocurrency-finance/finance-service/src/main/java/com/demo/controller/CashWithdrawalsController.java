package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.CashWithdrawAuditRecord;
import com.demo.domain.CashWithdrawals;
import com.demo.model.CashSellParam;
import com.demo.model.R;
import com.demo.service.CashWithdrawalsService;
import com.demo.util.ReportCsvUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cashWithdrawals")
@Api(tags = "GCN withdrawals record")
public class CashWithdrawalsController {

    @Autowired
    private CashWithdrawalsService cashWithdrawalsService;

    // Query cash withdrawals records by page
    @GetMapping("/records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "userId", value = "User id"),
            @ApiImplicitParam(name = "userName", value = "User name"),
            @ApiImplicitParam(name = "mobile", value = "Phone number"),
            @ApiImplicitParam(name = "status", value = "Status"),
            @ApiImplicitParam(name = "numMin", value = "Min amount"),
            @ApiImplicitParam(name = "numMax", value = "Max amount"),
            @ApiImplicitParam(name = "startTime", value = "Start time"),
            @ApiImplicitParam(name = "endTime", value = "End time"),
    })
    public R<Page<CashWithdrawals>> findByPage(
            @ApiIgnore Page<CashWithdrawals> page,
            Long userId, String userName, String mobile,
            Byte status, String numMin, String numMax,
            String startTime, String endTime) {
        Page<CashWithdrawals> pageData = cashWithdrawalsService.findByPage(page, userId, userName, mobile, status, numMin, numMax, startTime, endTime);
        return R.ok(pageData);
    }

    // Export cash withdrawals records
    @GetMapping("/records/export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "User id"),
            @ApiImplicitParam(name = "userName", value = "User name"),
            @ApiImplicitParam(name = "mobile", value = "Phone number"),
            @ApiImplicitParam(name = "status", value = "Status"),
            @ApiImplicitParam(name = "numMin", value = "Min amount"),
            @ApiImplicitParam(name = "numMax", value = "Max amount"),
            @ApiImplicitParam(name = "startTime", value = "Start time"),
            @ApiImplicitParam(name = "endTime", value = "End time"),
    })
    public void recordsExport(
            Long userId, String userName, String mobile,
            Byte status, String numMin, String numMax,
            String startTime, String endTime) {
        Page<CashWithdrawals> cashWithdrawalsPage = new Page<>(1, 10000);
        Page<CashWithdrawals> pageData = cashWithdrawalsService.findByPage(cashWithdrawalsPage, userId, userName, mobile, status, numMin, numMax, startTime, endTime);
        List<CashWithdrawals> records = pageData.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String[] header = {"ID", "用户ID", "用户名", "提现金额(USDT)", "手续费", "到账金额", "提现开户名", "银行名称", "账户", "申请时间", "完成时间", "状态", "备注", "审核级数"};
            String[] properties = {"id", "userId", "username", "num", "fee", "mum", "truename", "bank", "bankCard", "created", "lastTime", "status", "remark", "step"};

            CellProcessorAdaptor longToStringAdapter = new CellProcessorAdaptor() {
                @Override
                public <T> T execute(Object o, CsvContext csvContext) {
                    return (T) String.valueOf(o);
                }
            };

            // Money format
            DecimalFormat decimalFormat = new DecimalFormat("0.00000000");
            CellProcessorAdaptor moneyCellProcessorAdaptor = new CellProcessorAdaptor() {
                @Override
                public <T> T execute(Object o, CsvContext csvContext) {
                    BigDecimal num = (BigDecimal) o;
                    String numReal = decimalFormat.format(num);
                    return (T) numReal;
                }
            };

            // Date format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CellProcessorAdaptor timeCellProcessorAdaptor = new CellProcessorAdaptor() {
                @Override
                public <T> T execute(Object o, CsvContext csvContext) {
                    if (o == null) {
                        return (T) "";
                    }
                    Date date = (Date) o;
                    String dateStr = simpleDateFormat.format(date);
                    return (T) dateStr;
                }
            };

            // Status format
            CellProcessorAdaptor statusCellProcessorAdaptor = new CellProcessorAdaptor() {
                @Override
                public <T> T execute(Object o, CsvContext csvContext) {
                    Integer status = Integer.valueOf(String.valueOf(o));
                    String statusStr = "";
                    switch (status) {
                        case 0:
                            statusStr = "Waiting for review";
                            break;
                        case 1:
                            statusStr = "Passed";
                            break;
                        case 2:
                            statusStr = "Rejected";
                            break;
                        case 3:
                            statusStr = "Success";
                            break;
                        default:
                            statusStr = "Unknown";
                            break;
                    }
                    return (T) statusStr;
                }
            };

            CellProcessor[] cellProcessors = new CellProcessor[] {
                    longToStringAdapter, longToStringAdapter, null,
                    moneyCellProcessorAdaptor, moneyCellProcessorAdaptor, moneyCellProcessorAdaptor,
                    null, null, null,
                    timeCellProcessorAdaptor, timeCellProcessorAdaptor,
                    statusCellProcessorAdaptor,
                    null, null
            };

            try {
                ReportCsvUtils.reportListCsv(requestAttributes.getResponse(), header, properties, "Cash withdrawals record.csv", records, cellProcessors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Query user cash withdrawals records
    @GetMapping("/user/records")
    @ApiOperation(value = "Query user cash withdrawals records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "status", value = "Status"),
    })
    public R<Page<CashWithdrawals>> findUserCashRecharge(@ApiIgnore Page<CashWithdrawals> page, Byte status) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CashWithdrawals> cashWithdrawalsPage = cashWithdrawalsService.findCashWithdrawals(page, userId, status);
        return R.ok(cashWithdrawalsPage);
    }

    // Sell GCN
    @PostMapping("/sell")
    @ApiOperation(value = "Sell GCN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "", value = "")
    })
    public R<Object> sell(@RequestBody @Validated CashSellParam cashSellParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = cashWithdrawalsService.sell(userId, cashSellParam);
        if (isOk) {
            return R.ok("Request submitted successfully");
        }
        return R.fail("Request failed");
    }

    // Update cash withdrawal status
    @PostMapping("/updateWithdrawalsStatus")
    public R updateWithdrawalsStatus(@RequestBody CashWithdrawAuditRecord cashWithdrawAuditRecord) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk =  cashWithdrawalsService.updateWithdrawalsStatus(userId ,cashWithdrawAuditRecord);
        return isOk ? R.ok() : R.fail("Review failed");
    }
}
