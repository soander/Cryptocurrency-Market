package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.service.TurnoverOrderService;
import com.demo.domain.TurnoverOrder;
import com.demo.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/turnoverOrders")
@Api(tags = "Turnover Order Controller")
public class TurnoverOrderController {

    @Autowired
    private TurnoverOrderService turnoverOrderService ;

    @GetMapping
    @ApiOperation(value = "Query user's turnover order list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "Current page"),
            @ApiImplicitParam(name = "size",value = "Page size"),
    })
    public R<Page<TurnoverOrder>> findByPage(@ApiIgnore Page<TurnoverOrder> page, String symbol, Integer type) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<TurnoverOrder> pageData = turnoverOrderService.findByPage(page, userId, symbol, type);
        return R.ok(pageData);
    }
}
