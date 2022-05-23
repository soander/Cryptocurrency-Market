package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// System role
@ApiModel(value="com-demo-domain-SysRole")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role")
public class SysRole {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Primary key")
    private Long id;

    // Role Name
    @TableField(value = "name")
    @ApiModelProperty(value="Role Name")
    @NotNull
    private String name;

    // Code
    @TableField(value = "code")
    @ApiModelProperty(value="Code")
    @NotNull
    private String code;

    // Description
    @TableField(value = "description")
    @ApiModelProperty(value="Description")
    private String description;

    // Creator
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    @ApiModelProperty(value="Creator")
    private Long createBy;

    // Modifier
    @TableField(value = "modify_by",fill = FieldFill.UPDATE )
    @ApiModelProperty(value="Modifier")
    private Long modifyBy;

    // Status: 0-close 1-start
    @TableField(value = "status")
    @ApiModelProperty(value="Status: 0-close 1-start")
    private Byte status;

    // Created time
    @TableField(value = "created" ,fill = FieldFill.INSERT)
    @ApiModelProperty(value="Created time")
    private Date created;

    // Updated time
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="Updated time")
    private Date lastUpdateTime;

    // Privilege
    @TableField(exist = false)
    @ApiModelProperty("Privilege")
    private List<SysPrivilege> privileges = Collections.emptyList();
}