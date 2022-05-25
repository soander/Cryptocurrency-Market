package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.UserBank;
import com.demo.dto.UserBankDto;
//import com.demo.feign.UserBankServiceFeign;
import com.demo.mappers.UserBankDtoMapper;
import com.demo.model.R;
import com.demo.service.UserBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userBanks")
@Api(tags = "Members' bank management")
//public class UserBankController implements UserBankServiceFeign {
public class UserBankController {

    @Autowired
    private UserBankService userBankService;

    @GetMapping
    @ApiOperation(value = "Query members' bank by page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usrId", value = "Member id"),
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size")
    })
    @PreAuthorize("hasAuthority('user_bank_query')")
    public R<Page<UserBank>> findByPage(Page<UserBank> page, Long usrId) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<UserBank> userBankPage = userBankService.findByPage(page ,usrId);
        return R.ok(userBankPage);
    }

    @PostMapping("/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "Bank id"),
            @ApiImplicitParam(name = "status" ,value = "Bank status"),
    })
    @ApiOperation(value = "Update bank status")
    public R updateStatus(Long id ,Byte status) {
        UserBank userBank = new UserBank();
        userBank.setId(id);
        userBank.setStatus(status);
        boolean updateById = userBankService.updateById(userBank);
        if(updateById) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @PatchMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userBank", value = "Bank's json"),
    })
    @ApiOperation(value = "Update bank")
    public R updateStatus(@RequestBody @Validated UserBank userBank) {
        boolean updateById = userBankService.updateById(userBank);
        if(updateById) {
            return R.ok() ;
        }
        return R.fail("Update fail") ;
    }

    @GetMapping("/current")
    @ApiOperation(value = "Query current user's bank")
    public R<UserBank> getCurrentUserBank() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserBank userBank = userBankService.getCurrentUserBank(userId);
        return R.ok(userBank);
    }

    @PostMapping("/bind")
    @ApiOperation(value = "Bind a bank")
    public R bindBank(@RequestBody @Validated UserBank userBank) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userBankService.bindBank(userId,userBank);
        if(isOk) {
            return R.ok();
        }
        return R.fail("Bind fail") ;
    }

//    @Override
//    public UserBankDto getUserBankInfo(Long userId) {
//        UserBank currentUserBank = userBankService.getCurrentUserBank(userId);
//        UserBankDto userBankDto = UserBankDtoMapper.INSTANCE.toConvertDto(currentUserBank);
//        return userBankDto ;
//    }
}
