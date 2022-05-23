package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

// System privilege
@ApiModel(value="com-demo-domain-SysPrivilege")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_privilege")
public class SysPrivilege {

    // Primary Key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Primary Key")
    private Long id;

    // Menu id
    @TableField(value = "menu_id")
    @ApiModelProperty(value="Menu id")
    @NotNull
    private Long menuId;

    // Function name
    @TableField(value = "name")
    @ApiModelProperty(value="Function name")
    @NotNull
    private String name;

    // Function description
    @TableField(value = "description")
    @ApiModelProperty(value="Function description")
    private String description;

    // The URL
    @TableField(value = "url")
    @ApiModelProperty(value="")
    private String url;

    // The method
    @TableField(value = "method")
    @ApiModelProperty(value="")
    private String method;

    // Creator
    @TableField(value = "create_by",fill=FieldFill.INSERT)
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

    // Whether it has privilege
    @TableField(exist = false)
    @ApiModelProperty(value="Has privilege")
    private int own;
}