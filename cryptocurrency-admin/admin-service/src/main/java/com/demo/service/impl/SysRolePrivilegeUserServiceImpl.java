package com.demo.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.SysRolePrivilegeUserMapper;
import com.demo.domain.SysRolePrivilegeUser;
import com.demo.service.SysRolePrivilegeUserService;

@Service
public class SysRolePrivilegeUserServiceImpl extends ServiceImpl<SysRolePrivilegeUserMapper, SysRolePrivilegeUser> implements SysRolePrivilegeUserService{

}
