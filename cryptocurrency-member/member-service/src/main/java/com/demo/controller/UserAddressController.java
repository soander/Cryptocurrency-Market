package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.UserAddress;
import com.demo.model.R;
import com.demo.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@Api(tags = "User wallet address")
@RequestMapping("/userAddress")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;


    @GetMapping
    @ApiOperation(value = "Query user's wallet address")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId" ,value = "User id"),
            @ApiImplicitParam(name = "current" ,value = "Current page"),
            @ApiImplicitParam(name = "size" ,value = "Page size")
    })
    public R<Page<UserAddress>> findByPage(@ApiIgnore Page<UserAddress> page, Long userId) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<UserAddress> userAddressPage = userAddressService.findByPage(page,userId);
        return R.ok(userAddressPage);
    }

    @GetMapping("/current")
    public R getCurrentUserAddress() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<UserAddress> userAddressList = userAddressService.getUserAddressByUserId(Long.valueOf(userId));
        return R.ok(userAddressList);
    }

    @GetMapping("/getCoinAddress/{coinId}")
    @ApiOperation(value = "Query user's wallet address by some coin id")
    public R<String> getCoinAddress(@PathVariable("coinId") Long coinId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserAddress userAddress = userAddressService.getUserAddressByUserIdAndCoinId(userId,coinId);
        return R.ok(userAddress.getAddress());
    }
}
