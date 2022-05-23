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

// System role privilege
@ApiModel(value="com-demo-domain-SysRolePrivilege")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role_privilege")
public class SysRolePrivilege {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    // Role id
    @TableField(value = "role_id")
    @ApiModelProperty(value="")
    private Long roleId;

    // Privilege id
    @TableField(value = "privilege_id")
    @ApiModelProperty(value="")
    private Long privilegeId;
}