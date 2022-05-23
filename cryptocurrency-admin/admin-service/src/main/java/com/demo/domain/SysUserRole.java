package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// System user role
@ApiModel(value="com-demo-domain-SysUserRole")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user_role")
public class SysUserRole {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Primary key")
    private Long id;

    // Role id
    @TableField(value = "role_id")
    @ApiModelProperty(value="Role id")
    private Long roleId;

    // User id
    @TableField(value = "user_id")
    @ApiModelProperty(value="User id")
    private Long userId;

    // Creator
    @TableField(value = "create_by")
    @ApiModelProperty(value="Creator")
    private Long createBy;

    // Modifier
    @TableField(value = "modify_by",fill = FieldFill.UPDATE)
    @ApiModelProperty(value="Modifier")
    private Long modifyBy;

    // Created time
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="Created time")
    private Date created;

    // Updated time
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="Updated time")
    private Date lastUpdateTime;
}