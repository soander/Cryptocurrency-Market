package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.SysUser;
import com.demo.domain.SysUserRole;
import com.demo.mapper.SysUserMapper;
import com.demo.service.SysUserRoleService;
import com.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    private SysUserRoleService sysUserRoleService ;

    /**
     * @param page
     * @param mobile
     * @param fullname
     * @return
     */
    @Override
    public Page<SysUser> findByPage(Page<SysUser> page, String mobile, String fullname) {
        Page<SysUser> pageData = page(page,
                new LambdaQueryWrapper<SysUser>()
                        .like(!StringUtils.isEmpty(mobile), SysUser::getMobile, mobile)
                        .like(!StringUtils.isEmpty(fullname), SysUser::getFullname, fullname)
        );
        List<SysUser> records = pageData.getRecords();
        if(!CollectionUtils.isEmpty(records)) {
            for (SysUser record : records) {
                List<SysUserRole> userRoles = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, record.getId()));
                if(!CollectionUtils.isEmpty(userRoles)) {
                    record.setRole_strings(
                            userRoles.stream().
                                    map(sysUserRole -> sysUserRole.getRoleId().toString())
                                    .collect(Collectors.joining(",")));
                }
            }
        }
        return pageData;
    }

    /**
     * @param sysUser
     * @return
     */
    @Override
    @Transactional
    public boolean addUser(SysUser sysUser) {
        String password = sysUser.getPassword();

        String role_strings = sysUser.getRole_strings();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(password);
        sysUser.setPassword(encode);
        boolean save = super.save(sysUser);
        if(save) {
            if(!StringUtils.isEmpty(role_strings)) {
                String[] roleIds = role_strings.split(",");
                List<SysUserRole> sysUserRoleList = new ArrayList<>(roleIds.length);
                for (String roleId : roleIds) {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setRoleId(Long.valueOf(roleId));
                    sysUserRole.setUserId(sysUser.getId());
                    sysUserRoleList.add(sysUserRole);
                }
                sysUserRoleService.saveBatch(sysUserRoleList);
            }
        }
        return save;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        boolean b = super.removeByIds(idList);
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId,idList));
        return b;
    }
}
