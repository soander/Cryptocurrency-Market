package com.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// System user role privilege
@ApiModel(value="com-demo-domain-SysRolePrivilegeUser")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role_privilege_user")
public class SysRolePrivilegeUser {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    // Role id
    @TableField(value = "role_id")
    @ApiModelProperty(value="Role id")
    private Long roleId;

    // User id
    @TableField(value = "user_id")
    @ApiModelProperty(value="User id")
    private Long userId;

    // Privilege id
    @TableField(value = "privilege_id")
    @ApiModelProperty(value="Privilege id")
    private Long privilegeId;
}