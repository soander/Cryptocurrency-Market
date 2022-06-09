package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.service.EntrustOrderService;
import com.demo.domain.EntrustOrder;
import com.demo.model.R;
import com.demo.param.OrderParam;
import com.demo.vo.TradeEntrustOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/entrustOrders")
@Api(tags = "EntrustOrderController")
public class EntrustOrderController {

    @Autowired
    private EntrustOrderService entrustOrderService ;

    @GetMapping
    @ApiOperation(value = "Query user's entrust order list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "symbol", value = "Symbol"),
            @ApiImplicitParam(name = "type", value = "type"),
    })
    public R<Page<EntrustOrder>> findByPage(@ApiIgnore Page<EntrustOrder> page, String symbol, Integer type) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<EntrustOrder> entrustOrderPage = entrustOrderService.findByPage(page,userId,symbol,type);
        return R.ok(entrustOrderPage);
    }

    @GetMapping("/history/{symbol}")
    @ApiOperation(value = "Query user's entrust order history list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
    })
    public R<Page<TradeEntrustOrderVo>> historyEntrustOrder(@ApiIgnore Page<EntrustOrder> page , @PathVariable("symbol") String symbol) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<TradeEntrustOrderVo> pageData = entrustOrderService.getHistoryEntrustOrder(page,symbol,userId);
        return R.ok(pageData);
    }

    @GetMapping("/{symbol}")
    @ApiOperation(value = "Query user's unfinished entrust order list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "Current page"),
            @ApiImplicitParam(name = "size",value = "Page size"),
    })
    public R<Page<TradeEntrustOrderVo>> entrustOrders(@ApiIgnore Page<EntrustOrder> page , @PathVariable("symbol") String symbol) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<TradeEntrustOrderVo> pageData = entrustOrderService.getEntrustOrder(page, symbol, userId);
        return R.ok(pageData);
    }

    @PostMapping
    @ApiOperation(value = "Entrusted order operation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderParam",value = "orderParam's json")
    })
    public R createEntrustOrder(@RequestBody  OrderParam orderParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Boolean isOk = entrustOrderService.createEntrustOrder(userId,orderParam);
        return isOk ? R.ok() :R.fail("Create fail");
    }


    @ApiOperation(value = "委托单的取消操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "委托单的id")
    })
    @DeleteMapping("/{id}")
    public R deleteEntrustOrder(@PathVariable("id") Long orderId){
        entrustOrderService.cancleEntrustOrder(orderId) ;
        return R.ok("取消成功") ;
    }
}
