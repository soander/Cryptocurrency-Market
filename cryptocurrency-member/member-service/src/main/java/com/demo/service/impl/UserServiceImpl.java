package com.demo.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.config.IdAutoConfiguration;
import com.demo.domain.Sms;
import com.demo.domain.User;
import com.demo.domain.UserAuthAuditRecord;
import com.demo.domain.UserAuthInfo;
import com.demo.dto.UserDto;
import com.demo.geetest.GeetestLib;
import com.demo.mapper.UserMapper;
import com.demo.mappers.UserDtoMapper;
import com.demo.model.*;
import com.demo.service.SmsService;
import com.demo.service.UserAuthAuditRecordService;
import com.demo.service.UserAuthInfoService;
import com.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;
    @Autowired
    private UserAuthInfoService userAuthInfoService;
    @Autowired
    private GeetestLib geetestLib;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SmsService smsService;

    @Override
    public Page<User> findByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer status, Integer reviewStatus) {
        return page(page, new LambdaQueryWrapper<User>()
                        .like(!StringUtils.isEmpty(mobile), User::getMobile, mobile)
                        .like(!StringUtils.isEmpty(userName), User::getUsername, userName)
                        .like(!StringUtils.isEmpty(realName), User::getRealName, realName)
                        .eq(userId != null, User::getId, userId)
                        .eq(status != null, User::getStatus, status)
                        .eq(reviewStatus != null, User::getReviewsStatus, reviewStatus)
        );
    }

    public Page<User> findDirectInvitePage(Page<User> page, Long userId) {
        return page(page, new LambdaQueryWrapper<User>().eq(User::getDirectInviteid, userId));
    }

    // Update user authorization status
    @Override
    @Transactional
    public void updateUserAuthStatus(Long id, Byte authStatus, Long authCode, String remark) {
        log.info("Update user authorization status, user{}, Status{}, Code{}", id, authStatus, authCode);
        User user = getById(id);
        if (user != null) {
            user.setReviewsStatus(authStatus.intValue());
            updateById(user);
        }
        UserAuthAuditRecord userAuthAuditRecord = new UserAuthAuditRecord();
        userAuthAuditRecord.setUserId(id);
        userAuthAuditRecord.setStatus(authStatus);
        userAuthAuditRecord.setAuthCode(authCode);

        String usrStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userAuthAuditRecord.setAuditUserId(Long.valueOf(usrStr));
        userAuthAuditRecord.setAuditUserName("---------------------------");// 审核人的名称 --> 远程调用admin-service ,没有事务
        userAuthAuditRecord.setRemark(remark);

        userAuthAuditRecordService.save(userAuthAuditRecord);
    }

    // User identify verify
    @Override
    public boolean identifyVerify(Long id, UserAuthForm userAuthForm) {
        User user = getById(id);
        Assert.notNull(user, "User doesn't exist.");
        Byte authStatus = user.getAuthStatus();
        if (!authStatus.equals((byte) 0)) {
            throw new IllegalArgumentException("User has been verified.");
        }
        checkForm(userAuthForm);
        boolean check = IdAutoConfiguration.check(userAuthForm.getRealName(), userAuthForm.getIdCard());
        if (!check) {
            throw new IllegalArgumentException("User identify information error.");
        }

        // Set user identify information
        user.setAuthtime(new Date());
        user.setAuthStatus((byte) 1);
        user.setRealName(userAuthForm.getRealName());
        user.setIdCard(userAuthForm.getIdCard());
        user.setIdCardType(userAuthForm.getIdCardType());
        return updateById(user);
    }

    private void checkForm(UserAuthForm userAuthForm) {
        userAuthForm.check(geetestLib, redisTemplate);
    }

    @Override
    public User getById(Serializable id) {
        User user = super.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("Please input valid user id.");
        }
        Byte seniorAuthStatus = null;
        String seniorAuthDesc = "";
        Integer reviewsStatus = user.getReviewsStatus();
        if (reviewsStatus == null) {
            seniorAuthStatus = 3;
            seniorAuthDesc = "Information didn't update.";
        } else {
            switch (reviewsStatus) {
                case 1:
                    seniorAuthStatus = 1;
                    seniorAuthDesc = "Approved";
                    break;
                case 2:
                    seniorAuthStatus = 2;
                    List<UserAuthAuditRecord> userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(user.getId());
                    if (!CollectionUtils.isEmpty(userAuthAuditRecordList)) {
                        UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordList.get(0);
                        seniorAuthDesc = userAuthAuditRecord.getRemark();
                    }
                    break;
                case 0:
                    seniorAuthStatus = 0;
                    seniorAuthDesc = "Pending approve";
                    break;
            }
        }
        user.setSeniorAuthStatus(seniorAuthStatus);
        user.setSeniorAuthDesc(seniorAuthDesc);
        return user;
    }

    // User advanced authorization
    @Override
    @Transactional
    public void authUser(Long id, List<String> imgs) {
        if (CollectionUtils.isEmpty(imgs)) {
            throw new IllegalArgumentException("User information is null.");
        }
        User user = getById(id);
        if (user == null) {
            throw new IllegalArgumentException("Please input valid user id");
        }
        long authCode = snowflake.nextId();
        List<UserAuthInfo> userAuthInfoList = new ArrayList<>(imgs.size());
        for (int i = 0; i < imgs.size(); i++) {
            UserAuthInfo userAuthInfo = new UserAuthInfo();
            userAuthInfo.setImageUrl(imgs.get(i));
            userAuthInfo.setUserId(id);
            userAuthInfo.setSerialno(i + 1);
            userAuthInfo.setAuthCode(authCode);
            userAuthInfoList.add(userAuthInfo);
        }
        userAuthInfoService.saveBatch(userAuthInfoList);
        user.setReviewsStatus(0);
        updateById(user); // Update user status
    }

    // Update user phone number
    @Override
    public boolean updatePhone(Long userId, UpdatePhoneParam updatePhoneParam) {
        User user = getById(userId);

        // Old phone
        String oldMobile = user.getMobile();
        String oldMobileCode = stringRedisTemplate.opsForValue().get("SMS:VERIFY_OLD_PHONE:" + oldMobile);
        if (!updatePhoneParam.getOldValidateCode().equals(oldMobileCode)) {
            throw new IllegalArgumentException("Old phone number sms code is wrong.");
        }

        // New phone
        String newPhoneCode = stringRedisTemplate.opsForValue().get("SMS:CHANGE_PHONE_VERIFY:" + updatePhoneParam.getNewMobilePhone());
        if (!updatePhoneParam.getValidateCode().equals(newPhoneCode)) {
            throw new IllegalArgumentException("New phone number sms code is wrong.");
        }
        // Update phone number
        user.setMobile(updatePhoneParam.getNewMobilePhone());
        return updateById(user);
    }

    // Check new phone number validation
    @Override
    public boolean checkNewPhone(String mobile, String countryCode) {

        int count = count(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile).eq(User::getCountryCode, countryCode));
        if (count > 0) {
            throw new IllegalArgumentException("The phone number has been registered.");
        }

        Sms sms = new Sms();
        sms.setMobile(mobile);
        sms.setCountryCode(countryCode);
        sms.setTemplateCode("CHANGE_PHONE_VERIFY");
        return smsService.sendSms(sms);
    }

    // Update user password
    @Override
    public boolean updateUserLoginPwd(Long userId, UpdateLoginParam updateLoginParam) {
        User user = getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User id is wrong.");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(updateLoginParam.getOldpassword(), user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("User input password is wrong.");
        }

        String validateCode = updateLoginParam.getValidateCode();
        String phoneValidateCode = stringRedisTemplate.opsForValue().get("SMS:CHANGE_LOGIN_PWD_VERIFY:" + user.getMobile());//"SMS:CHANGE_LOGIN_PWD_VERIFY:111111"
        if (!validateCode.equals(phoneValidateCode)) {
            throw new IllegalArgumentException("Sms code is wrong.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(updateLoginParam.getNewpassword()));
        return updateById(user);
    }

    // Update pay password
    @Override
    public boolean updateUserPayPwd(Long userId, UpdateLoginParam updateLoginParam) {

        User user = getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User id is wrong.");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        boolean matches = bCryptPasswordEncoder.matches(updateLoginParam.getOldpassword(), user.getPaypassword());
        if (!matches) {
            throw new IllegalArgumentException("User input password is wrong.");
        }

        String validateCode = updateLoginParam.getValidateCode();
        String phoneValidateCode = stringRedisTemplate.opsForValue().get("SMS:CHANGE_PAY_PWD_VERIFY:" + user.getMobile());//"SMS:CHANGE_LOGIN_PWD_VERIFY:111111"
        if (!validateCode.equals(phoneValidateCode)) {
            throw new IllegalArgumentException("Sms code is wrong.");
        }
        user.setPaypassword(bCryptPasswordEncoder.encode(updateLoginParam.getNewpassword()));
        return updateById(user);
    }

    // Set pay password
    @Override
    public boolean unsetPayPassword(Long userId, UnsetPayPasswordParam unsetPayPasswordParam) {
        User user = getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User id is wrong.");
        }
        String validateCode = unsetPayPasswordParam.getValidateCode();
        String phoneValidate = stringRedisTemplate.opsForValue().get("SMS:FORGOT_PAY_PWD_VERIFY:" + user.getMobile());
        if (!validateCode.equals(phoneValidate)) {
            throw new IllegalArgumentException("Sms code is wrong.");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPaypassword(bCryptPasswordEncoder.encode(unsetPayPasswordParam.getPayPassword()));

        return updateById(user);
    }

    // Get user invitation list
    @Override
    public List<User> getUserInvites(Long userId) {
        List<User> list = list(new LambdaQueryWrapper<User>().eq(User::getDirectInviteid, userId));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        list.forEach(user -> {
            user.setPaypassword("*********");
            user.setPassword("********");
            user.setAccessKeyId("*********");
            user.setAccessKeySecret("*********");
        });
        return list;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query user information by user ids
    * @Date 2022/5/29 16:41
    * @Param ids: User's ids
    * @Param userName: User's userName
    * @Param mobile: User's mobile
    * @Return * @return null
    **/
    @Override
    public Map<Long, UserDto> getBasicUsers(List<Long> ids, String userName, String mobile) {
        if (CollectionUtils.isEmpty(ids) && StringUtils.isEmpty(userName) && StringUtils.isEmpty(mobile)) {
            return Collections.emptyMap();
        }
        List<User> list = list(new LambdaQueryWrapper<User>()
                .in(!CollectionUtils.isEmpty(ids), User::getId, ids)
                .like(!StringUtils.isEmpty(userName), User::getUsername, userName)
                .like(!StringUtils.isEmpty(mobile), User::getMobile, mobile));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        // user->userDto
        List<UserDto> userDtos = UserDtoMapper.INSTANCE.convert2Dto(list);
        return userDtos.stream().collect(Collectors.toMap(UserDto::getId, userDto -> userDto));
    }

    /**
    * @Author Yaozheng Wang
    * @Description User registration
    * @Date 2022/5/31 9:15
    * @Param RegisterParam
    * @Return boolean
    **/
    @Override
    public boolean register(RegisterParam registerParam) {
        log.info("User register{}", JSON.toJSONString(registerParam, true));
        String mobile = registerParam.getMobile();
        String email = registerParam.getEmail();

        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(mobile)) {
            throw new IllegalArgumentException("Phone number or email is required.");
        }
        int count = count(new LambdaQueryWrapper<User>()
                .eq(!StringUtils.isEmpty(email), User::getEmail, email)
                .eq(!StringUtils.isEmpty(mobile), User::getMobile, mobile)
        );
        if (count > 0) {
            throw new IllegalArgumentException("Phone number or email has been registered.");
        }

        registerParam.check(geetestLib, redisTemplate);
        User user = getUser(registerParam); // Create a new user
        return save(user);
    }

    private User getUser(RegisterParam registerParam) {
        User user = new User();
        user.setCountryCode(registerParam.getCountryCode());
        user.setEmail(registerParam.getEmail());
        user.setMobile(registerParam.getMobile());
        String encodePwd = new BCryptPasswordEncoder().encode(registerParam.getPassword());
        user.setPassword(encodePwd);
        user.setPaypassSetting(false);
        user.setStatus((byte) 1);
        user.setType((byte) 1);
        user.setAuthStatus((byte) 0);
        user.setLogins(0);
        user.setInviteCode(RandomUtil.randomString(6));
        if (!StringUtils.isEmpty(registerParam.getInvitionCode())) {
            User userPre = getOne(new LambdaQueryWrapper<User>().eq(User::getInviteCode, registerParam.getInvitionCode()));
            if (userPre != null) {
                user.setDirectInviteid(String.valueOf(userPre.getId()));
                user.setInviteRelation(String.valueOf(userPre.getId()));
            }
        }
        return user;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Reset login password
    * @Date 2022/5/31 9:21
    * @Param setPasswordParam
    * @Return * @return null
    **/
    @Override
    public boolean unsetLoginPwd(UnSetPasswordParam unSetPasswordParam) {
        log.info("Set new password{}", JSON.toJSONString(unSetPasswordParam, true));
        unSetPasswordParam.check(geetestLib, redisTemplate);

        // Phone number check
        String phoneValidateCode = stringRedisTemplate.opsForValue().get("SMS:FORGOT_VERIFY:" + unSetPasswordParam.getMobile());
        if (!unSetPasswordParam.getValidateCode().equals(phoneValidateCode)) {
            throw new IllegalArgumentException("Phone number verification code is wrong.");
        }

        // Check database
        String mobile = unSetPasswordParam.getMobile();
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
        if (user == null) {
            throw new IllegalArgumentException("User is not exist.");
        }
        String encode = new BCryptPasswordEncoder().encode(unSetPasswordParam.getPassword());
        user.setPassword(encode);
        return updateById(user);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("c4ca4238a0b923820dcc509a6f75849b");// 我们在网页上的MD5(LTD12345)
        // $2a$10$ST0HQ4hZCRCMLGA8dDA96e7wzDMnBRR1rSTrD2z/LLVgivdArzF42-> 修改我们的数据库密码->替换为现在这个值
        System.out.println(encode);
    }
}