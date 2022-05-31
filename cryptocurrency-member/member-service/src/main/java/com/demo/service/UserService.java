package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.User;
import com.demo.dto.UserDto;
import com.demo.model.*;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    Page<User> findByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer status, Integer reviewStatus);

    Page<User> findDirectInvitePage(Page<User> page, Long userId);

    // Update user authorization status
    void updateUserAuthStatus(Long id, Byte authStatus, Long authCode, String remark);

    // User identify Verify
    boolean identifyVerify(Long id, UserAuthForm userAuthForm);

    // User advanced authorization
    void authUser(Long id, List<String> imgs);

    // Update user phone number
    boolean updatePhone(Long userId, UpdatePhoneParam updatePhoneParam);

    // Chceck new phone number invalidation
    boolean checkNewPhone(String mobile, String countryCode);

    // Update user password
    boolean updateUserLoginPwd(Long userId, UpdateLoginParam updateLoginParam);

    // Update pay password
    boolean updateUserPayPwd(Long userId, UpdateLoginParam updateLoginParam);

    // Set user pay password
    boolean unsetPayPassword(Long userId, UnsetPayPasswordParam unsetPayPasswordParam);

    // Get user invitation list
    List<User> getUserInvites(Long userId);

    // Query user basic info by userIds
    Map<Long, UserDto> getBasicUsers(List<Long> ids, String userName, String mobile);

    // User register
    boolean register(RegisterParam registerParam);

    // Reset login password
    boolean unsetLoginPwd(UnSetPasswordParam unSetPasswordParam);
}
