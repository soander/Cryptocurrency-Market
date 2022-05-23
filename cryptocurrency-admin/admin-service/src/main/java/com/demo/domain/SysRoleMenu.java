package com.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Sysrtem Role Menu
@ApiModel(value="com-demo-domain-SysRoleMenu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role_menu")
public class SysRoleMenu {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    // Role id
    @TableField(value = "role_id")
    @ApiModelProperty(value="")
    private Long roleId;

    // Menu id
    @TableField(value = "menu_id")
    @ApiModelProperty(value="")
    private Long menuId;

    // Creator
    @TableField(value = "create_by")
    @ApiModelProperty(value="Creator")
    private Long createBy;

    // Modifier
    @TableField(value = "modify_by")
    @ApiModelProperty(value="Modifier")
    private Long modifyBy;

    // Created time
    @TableField(value = "created")
    @ApiModelProperty(value="Created time")
    private Date created;

    // Updated time
    @TableField(value = "last_update_time")
    @ApiModelProperty(value="Updated time")
    private Date lastUpdateTime;
}