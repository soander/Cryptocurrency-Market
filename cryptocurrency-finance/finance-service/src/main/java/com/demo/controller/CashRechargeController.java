package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.CashRecharge;
import com.demo.domain.CashRechargeAuditRecord;
import com.demo.model.CashParam;
import com.demo.model.R;
import com.demo.service.CashRechargeService;
import com.demo.util.ReportCsvUtils;
import com.demo.vo.CashTradeVo;
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
@RequestMapping("/cashRecharges")
@Api(value = "GCN Recharge Controller")
public class CashRechargeController {

    @Autowired
    private CashRechargeService cashRechargeService;

    // Query cash recharge records by page
    @GetMapping("/records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "coinId", value = "Current page"),
            @ApiImplicitParam(name = "userId", value = "User id"),
            @ApiImplicitParam(name = "userName", value = "User name"),
            @ApiImplicitParam(name = "mobile", value = "Phone number"),
            @ApiImplicitParam(name = "status", value = "Status"),
            @ApiImplicitParam(name = "numMin", value = "Min number"),
            @ApiImplicitParam(name = "numMax", value = "Max number"),
            @ApiImplicitParam(name = "startTime", value = "Start time"),
            @ApiImplicitParam(name = "endTime", value = "End time"),
    })
    public R<Page<CashRecharge>> findByPage(
            @ApiIgnore Page<CashRecharge> page, Long coinId,
            Long userId, String userName, String mobile,
            Byte status, String numMin, String numMax,
            String startTime, String endTime
    ) {
        Page<CashRecharge> pageData = cashRechargeService.findByPage(page, coinId, userId, userName,
                mobile, status, numMin, numMax, startTime, endTime);
        return R.ok(pageData);
    }

    // Export cash recharge records
    @GetMapping("/records/export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "Current page"),
            @ApiImplicitParam(name = "userId", value = "User id"),
            @ApiImplicitParam(name = "userName", value = "User name"),
            @ApiImplicitParam(name = "mobile", value = "Mobile number"),
            @ApiImplicitParam(name = "status", value = "Status"),
            @ApiImplicitParam(name = "numMin", value = "Min number"),
            @ApiImplicitParam(name = "numMax", value = "Max number"),
            @ApiImplicitParam(name = "startTime", value = "Start time"),
            @ApiImplicitParam(name = "endTime", value = "End time"),
    })
    public void recordsExport(Long coinId, Long userId, String userName, String mobile,
                              Byte status, String numMin, String numMax,
                              String startTime, String endTime) {
        Page<CashRecharge> page = new Page<>(1, 10000);
        Page<CashRecharge> pageData = cashRechargeService.findByPage(page, coinId, userId, userName,
                mobile, status, numMin, numMax, startTime, endTime);
        List<CashRecharge> records = pageData.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            String[] header = {"ID", "用户ID", "用户名", "真实用户名", "充值币种", "充值金额(USDT)", "手续费", "到账金额(CNY)", "充值方式", "充值订单", "参考号", "充值时间", "完成时间", "状态", "审核备注", "审核级数"};
            String[] properties = {"id", "userId", "username", "realName", "coinName", "num", "fee", "mum", "type", "tradeno", "remark", "created", "lastTime", "status", "auditRemark", "step"};

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

            // Type format
            CellProcessorAdaptor typeAdapter = new CellProcessorAdaptor() {
                @Override
                public <T> T execute(Object o, CsvContext csvContext) {
                    String type = String.valueOf(o);
                    String typeName = "";
                    switch (type) {
                        case "alipay":
                            typeName = "AliPay";
                            break;
                        case "cai1pay":
                            typeName = "Cai1Pay";
                            break;
                        case "bank":
                            typeName = "Bank";
                            break;
                        case "linepay":
                            typeName = "OnlinePay";
                            break;
                        default:
                            typeName = "Unknown";
                            break;
                    }
                    return (T) typeName;
                }
            };

            // Date format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CellProcessorAdaptor timeCellProcessorAdaptor = new CellProcessorAdaptor() {
                @Override
                public <T> T execute(Object o, CsvContext csvContext) {
                    if(o ==null){
                        return (T)"" ;
                    }
                    Date date = (Date) o;
                    String dateStr = simpleDateFormat.format(date);
                    return (T) dateStr;
                }
            };

            // Status format
            CellProcessorAdaptor statusCellProcessorAdaptor =  new CellProcessorAdaptor() {
                @Override
                public <T> T execute(Object o, CsvContext csvContext) {
                    Integer status = Integer.valueOf(String.valueOf(o));
                    String statusStr = "";
                    switch (status){
                        case 0:
                            statusStr = "Waiting for audit";
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

            CellProcessor[] PROCESSOR = new CellProcessor[] {
                    longToStringAdapter,  longToStringAdapter, null, null, null,
                    moneyCellProcessorAdaptor, moneyCellProcessorAdaptor, moneyCellProcessorAdaptor, typeAdapter,
                    null, null, timeCellProcessorAdaptor, timeCellProcessorAdaptor,
                    statusCellProcessorAdaptor, null, null

            };
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            try {
                ReportCsvUtils.reportListCsv(requestAttributes.getResponse(), header, properties, "Cash recharge records.csv", records, PROCESSOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/user/records")
    @ApiOperation(value = "Query user cash recharge records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "status", value = "Status"),
    })
    public R<Page<CashRecharge>> findUserCashRecharge(@ApiIgnore Page<CashRecharge> page, Byte status) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CashRecharge> cashRechargePage = cashRechargeService.findUserCashRecharge(page, userId, status);
        return R.ok(cashRechargePage);
    }

    @PostMapping("/buy")
    @ApiOperation(value = "buy GCN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cashParam", value = "Cash param"),
    })
    public R<CashTradeVo> buy(@RequestBody @Validated CashParam cashParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        CashTradeVo cashTradeVo = cashRechargeService.buy(userId, cashParam);
        return R.ok(cashTradeVo);
    }

    @ApiOperation(value = "Cash recharge Audit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cashRechargeAuditRecord" ,value = "Cash recharge Audit")
    })
    @PostMapping("/cashRechargeUpdateStatus")
    public R cashRechargeUpdateStatus(@RequestBody CashRechargeAuditRecord cashRechargeAuditRecord) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
       boolean isOk = cashRechargeService.cashRechargeAudit(userId,cashRechargeAuditRecord);
      return isOk ? R.ok():R.fail("Audit failed");
    }
}
