package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

// System User
@ApiModel(value="com-demo-domain-SysUser")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class SysUser {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    // User account name
    @TableField(value = "username")
    @ApiModelProperty(value="User account name")
    @NotBlank
    private String username;

    // User account password
    @TableField(value = "password")
    @ApiModelProperty(value="User account password")
    @NotBlank
    private String password;

    // User full name
    @TableField(value = "fullname")
    @ApiModelProperty(value="User full name")
    private String fullname;

    // User mobile number
    @TableField(value = "mobile")
    @ApiModelProperty(value="User mobile number")
    @NotBlank
    private String mobile;

    // User email
    @TableField(value = "email")
    @ApiModelProperty(value="User email")
    @NotBlank
    private String email;

    // Status: 0-invalid 1-valid
    @TableField(value = "status")
    @ApiModelProperty(value="Status: 0-invlaid 1-valid")
    private Byte status;

    // Creator
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建人")
    private Long createBy;

    // Modifier
    @TableField(value = "modify_by",fill = FieldFill.UPDATE)
    @ApiModelProperty(value="修改人")
    private Long modifyBy;

    // Created time
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date created;

    // Updated time
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="修改时间")
    private Date lastUpdateTime;

    // User IDS
    @ApiModelProperty(value = "角色的IDS")
    @TableField(exist = false)
    private String role_strings ;
}