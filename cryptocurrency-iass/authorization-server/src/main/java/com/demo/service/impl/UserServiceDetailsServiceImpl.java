package com.demo.service.impl;

import com.demo.constant.LoginConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.demo.constant.LoginConstant.*;

/**
 * @ClassName UserServiceDetailsServiceImpl
 * @Description User service
 * @Author Yaozheng Wang
 * @Date 2022/5/18 14:43
 **/
@Service
public class UserServiceDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String loginType = requestAttributes.getRequest().getParameter("login_type");
        if (StringUtils.isEmpty(loginType)) {
            throw new AuthenticationServiceException("Please add login_type parameter");
        }
        String grantType = requestAttributes.getRequest().getParameter("grant_type");
        UserDetails userDetails;
        try {
            if (LoginConstant.REFRESH_TOKEN.equals(grantType.toUpperCase())) {
                username = adjustUsername(username, loginType); // fresh_token change id to username
            }
            switch (loginType) {
                case LoginConstant.ADMIN_TYPE: // Admin login
                    userDetails = loadAdminUserByUsername(username);
                    break;
                case LoginConstant.MEMBER_TYPE: // Member login
                    userDetails = loadMemberUserByUsername(username);
                    break;
                default:
                    throw new AuthenticationServiceException("Not support the login type: " + loginType);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UsernameNotFoundException("Member: " + username + " doesn't exist.");
        }
        return userDetails;
    }

    /**
    * @Author Yaozheng Wang
    * @Description The admin user login
    * @Date 2022/5/18 14:50
    * @Param String username
    * @Return * @return null
    **/
    private UserDetails loadAdminUserByUsername(String username) {
        return jdbcTemplate.queryForObject(QUERY_ADMIN_SQL, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs.wasNull()) {
                    throw new UsernameNotFoundException("User: " + username + "doesn't exist");
                }
                Long id = rs.getLong("id"); // Admin id
                String password = rs.getString("password"); // Admin password
                int status = rs.getInt("status"); // Admin status
                User user = new User(
                        String.valueOf(id),
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        getUserPermissions(id));
                return user;
            }
        }, username);
    }

    // Check user permission
    private Set<SimpleGrantedAuthority> getUserPermissions(Long id) {
        String code = jdbcTemplate.queryForObject(QUERY_ROLE_CODE_SQL, String.class, id);
        List<String> permissions;
        if (ADMIN_CODE.equals(code)) { // Admin user
            permissions = jdbcTemplate.queryForList(QUERY_ALL_PERMISSIONS, String.class);
        } else { // Member user
            permissions = jdbcTemplate.queryForList(QUERY_PERMISSION_SQL, String.class, id);
        }
        if (permissions == null || permissions.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        return permissions
                .stream()
                .distinct()
                .map(perm -> new SimpleGrantedAuthority(perm))
                .collect(Collectors.toSet());
    }

    /**
    * @Author Yaozheng Wang
    * @Description The member user login
    * @Date 2022/5/18 14:51
    * @Param String username
    * @Return * @return null
    **/
    private UserDetails loadMemberUserByUsername(String username) {
        return jdbcTemplate.queryForObject(QUERY_MEMBER_SQL, new RowMapper<UserDetails>() {
            @Override
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs.wasNull()) {
                    throw new UsernameNotFoundException("Member: " + username + " doesn't exist");
                }
                long id = rs.getLong("id"); // Member id
                String password = rs.getString("password"); // Member password
                int status = rs.getInt("status"); // Member status
                return new User(
                        String.valueOf(id),
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
        }, username, username);
    }

    /**
    * @Author Yaozheng Wang
    * @Description The adjust method
    * @Date 2022/5/18 16:26
    * @Param String username and String login type
    * @Return * @return username
    **/
    private String adjustUsername(String username, String loginType) {
        if (LoginConstant.ADMIN_TYPE.equals(loginType)) { // Adjust the admin type
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_USER_WITH_ID, String.class, username);
        }
        if (LoginConstant.MEMBER_TYPE.equals(loginType)) { // Adjust the member type
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_USER_WITH_ID, String.class, username);
        }
        return username;
    }
}
