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

import java.util.Date;

// System log
@ApiModel(value="com-demo-domain-SysUserLog")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user_log")
public class SysUserLog {

    // Primary key
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="Primary key")
    private Long id;

    // Group
    @TableField(value = "`group`")
    @ApiModelProperty(value="Group")
    private String group;

    // User id
    @TableField(value = "user_id")
    @ApiModelProperty(value="User id")
    private Long userId;

    // Log type: 1-search 2-update 3-add 4-delete 5-import 6-check
    @TableField(value = "type")
    @ApiModelProperty(value="Log type: 1-search 2-update 3-add 4-delete 5-import 6-check")
    private Short type;

    // Method
    @TableField(value = "method")
    @ApiModelProperty(value="Method")
    private String method;

    // Parameter
    @TableField(value = "params")
    @ApiModelProperty(value="Parameter")
    private String params;

    // Time
    @TableField(value = "time")
    @ApiModelProperty(value="Time")
    private Long time;

    // Ip address
    @TableField(value = "ip")
    @ApiModelProperty(value="Ip address")
    private String ip;

    // Description
    @TableField(value = "description")
    @ApiModelProperty(value="Description")
    private String description;

    // Remark
    @TableField(value = "remark")
    @ApiModelProperty(value="Remark")
    private String remark;

    // Created time
    @TableField(value = "created")
    @ApiModelProperty(value="// Created time")
    private Date created;
}