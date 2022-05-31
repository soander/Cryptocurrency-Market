package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.User;
import com.demo.domain.UserAuthAuditRecord;
import com.demo.domain.UserAuthInfo;
//import com.demo.dto.UserDto;
//import com.demo.feign.UserServiceFeign;
import com.demo.dto.UserDto;
import com.demo.feign.UserServiceFeign;
import com.demo.model.*;
import com.demo.service.UserAuthAuditRecordService;
import com.demo.service.UserAuthInfoService;
import com.demo.service.UserService;
import com.demo.valueObject.UseAuthInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Api(tags = "User controller")
public class UserController implements UserServiceFeign {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @GetMapping
    @ApiOperation(value = "Query member by page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "mobile", value = "Member phone"),
            @ApiImplicitParam(name = "userId", value = "Member id"),
            @ApiImplicitParam(name = "userName", value = "Member username"),
            @ApiImplicitParam(name = "realName", value = "Member real name"),
            @ApiImplicitParam(name = "status", value = "Member status")

    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<Page<User>> findByPage(@ApiIgnore Page<User> page, String mobile, Long userId,
                                    String userName, String realName, Integer status) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<User> userPage = userService.findByPage(page, mobile, userId, userName, realName, status, null);
        return R.ok(userPage);
    }

    @PostMapping("/status")
    @ApiOperation(value = "Update member status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Member id"),
            @ApiImplicitParam(name = "status", value = "Member status"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(Long id, Byte status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        boolean updateById = userService.updateById(user);
        if (updateById) {
            return R.ok("Update success");
        }
        return R.fail("Update fail");
    }

    @PatchMapping
    @ApiOperation(value = "Update memeber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "Member's json"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(@RequestBody @Validated User user) {
        boolean updateById = userService.updateById(user);
        if (updateById) {
            return R.ok("Update success");
        }
        return R.fail("Updata fail");
    }

    @GetMapping("/info")
    @ApiOperation(value = "Query members' detail information")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Member id")
    })
    public R<User> userInfo(Long id) {
        User user = userService.getById(id);
        return R.ok(user);
    }

    @GetMapping("/directInvites")
    @ApiOperation(value = "Query user's invitation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "Current user id"),
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),

    })
    public R<Page<User>> getDirectInvites(@ApiIgnore Page<User> page, Long userId) {
        Page<User> userPage = userService.findDirectInvitePage(page, userId);
        return R.ok(userPage);
    }

    @GetMapping("/auths")
    @ApiOperation(value = "Query user's authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "Current page"),
            @ApiImplicitParam(name = "size", value = "Page size"),
            @ApiImplicitParam(name = "mobile", value = "Member phone"),
            @ApiImplicitParam(name = "userId", value = "User id"),
            @ApiImplicitParam(name = "userName", value = "User name"),
            @ApiImplicitParam(name = "realName", value = "User real name"),
            @ApiImplicitParam(name = "reviewsStatus", value = "User status")

    })
    public R<Page<User>> findUserAuths(@ApiIgnore Page<User> page, String mobile, Long userId,
                                       String userName, String realName, Integer reviewsStatus) {
        Page<User> userPage = userService.findByPage(page, mobile, userId, userName, realName, null, reviewsStatus);
        return R.ok(userPage);
    }

    @GetMapping("/auth/info")
    @ApiOperation(value = "Query user's authorization information")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User id")
    })
    public R<UseAuthInfoVo> getUseAuthInfo(Long id) {
        User user = userService.getById(id);
        List<UserAuthAuditRecord> userAuthAuditRecordList = null;
        List<UserAuthInfo> userAuthInfoList = null;
        if (user != null) {
            // User's review status 0-Unreviewed
            Integer reviewsStatus = user.getReviewsStatus();
            if (reviewsStatus == null || reviewsStatus == 0) {
                userAuthAuditRecordList = Collections.emptyList();
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByUserId(id);
            } else {
                userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(id);
                UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordList.get(0);
                Long authCode = userAuthAuditRecord.getAuthCode();
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByCode(authCode);
            }
        }
        return R.ok(new UseAuthInfoVo(user, userAuthInfoList, userAuthAuditRecordList));
    }

    @PostMapping("/auths/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User id"),
            @ApiImplicitParam(name = "authStatus", value = "User authorization status"),
            @ApiImplicitParam(name = "authCode", value = "Authorization code"),
            @ApiImplicitParam(name = "remark", value = "Refuse reason"),
    })
    public R updateUserAuthStatus(@RequestParam(required = true) Long id, @RequestParam(required = true) Byte authStatus, @RequestParam(required = true) Long authCode, String remark) {
        userService.updateUserAuthStatus(id, authStatus, authCode, remark);
        return R.ok();
    }

    @GetMapping("/current/info")
    @ApiOperation(value = "Current user information")
    public R<User> currentUserInfo() {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getById(Long.valueOf(idStr));
        user.setPassword("****");
        user.setPaypassword("***");
        user.setAccessKeyId("****");
        user.setAccessKeySecret("******");
        return R.ok(user);
    }

    @PostMapping("/authAccount")
    @ApiOperation(value = "User information authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAuthForm", value = "userAuthForm json")
    })
    public R identifyCheck(@RequestBody UserAuthForm userAuthForm) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.identifyVerify(Long.valueOf(idStr), userAuthForm);
        if (isOk) {
            return R.ok();
        }
        return R.fail("Authorization fail");
    }

    @PostMapping("/authUser")
    @ApiOperation(value = "Advanced authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgs", value = "User images")
    })
    public R authUser(@RequestBody String[] imgs) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.authUser(Long.valueOf(idStr), Arrays.asList(imgs));
        return R.ok();
    }

    @PostMapping("/updatePhone")
    @ApiOperation(value = "Change phone number")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updatePhoneParam", value = "updatePhoneParam's json")
    })
    public R updatePhone(@RequestBody UpdatePhoneParam updatePhoneParam) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.updatePhone(Long.valueOf(idStr), updatePhoneParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @GetMapping("/checkTel")
    @ApiOperation(value = "Check phone number validation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "New phone number"),
            @ApiImplicitParam(name = "countryCode", value = "New phone number's country code")
    })
    public R checkNewPhone(@RequestParam(required = true) String mobile, @RequestParam(required = true) String countryCode) {
        boolean isOk = userService.checkNewPhone(mobile, countryCode);
        return isOk ? R.ok() : R.fail("New phone number is invalid.");
    }

    @PostMapping("/updateLoginPassword")
    @ApiOperation(value = "Update user password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updateLoginParam", value = "Update user password")
    })
    public R updateLoginPwd(@RequestBody @Validated UpdateLoginParam updateLoginParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userService.updateUserLoginPwd(userId, updateLoginParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @PostMapping("/updatePayPassword")
    @ApiOperation(value = "Update pay password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updateLoginParam", value = "Update pay password")
    })
    public R updatePayPwd(@RequestBody @Validated UpdateLoginParam updateLoginParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userService.updateUserPayPwd(userId, updateLoginParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @PostMapping("/setPayPassword")
    @ApiOperation(value = "Set pay password")
    public R setPayPassword(@RequestBody @Validated UnsetPayPasswordParam unsetPayPasswordParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userService.unsetPayPassword(userId, unsetPayPasswordParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("Set fail");
    }

    @GetMapping("/invites")
    @ApiOperation(value = "User invitation list")
    public R<List<User>> getUserInvites() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<User> users = userService.getUserInvites(userId);
        return R.ok(users);
    }

    @PostMapping("/register")
    @ApiOperation(value = "User register")
    public R register(@RequestBody RegisterParam registerParam) {
        boolean isOk = userService.register(registerParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("Register fail");
    }

    @PostMapping("/setPassword")
    @ApiOperation(value = "User set password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unSetPasswordParam", value = "unSetPasswordParam json")
    })
    public R unsetPassword(@RequestBody @Validated UnSetPasswordParam unSetPasswordParam) {
        boolean isOk = userService.unsetLoginPwd(unSetPasswordParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("Set fail");
    }

    // Admin-service uses Member-service
    @Override
    public Map<Long, UserDto> getBasicUsers(List<Long> ids, String userName, String mobile) {
        Map<Long, UserDto> userDtos = userService.getBasicUsers(ids, userName, mobile);
        return userDtos;
    }

}
