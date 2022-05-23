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
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

// System menu
@ApiModel(value="com-demo-domain-SysMenu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_menu")
public class SysMenu {

    // Primary Key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Primary Key")
    private Long id;

    // Parent id
    @TableField(value = "parent_id")
    @ApiModelProperty(value="Parent id")
    private Long parentId;

    // Parent key
    @TableField(value = "parent_key")
    @ApiModelProperty(value="Parent key")
    private String parentKey;

    // Type: 1-class 2-note
    @TableField(value = "type")
    @ApiModelProperty(value="type")
    private Byte type;

    // Name
    @TableField(value = "name")
    @ApiModelProperty(value="Name")
    private String name;

    // Description
    @TableField(value = "`desc`")
    @ApiModelProperty(value="Description")
    private String desc;

    // Target URL
    @TableField(value = "target_url")
    @ApiModelProperty(value="Target URL")
    private String targetUrl;

    // Sorting index
    @TableField(value = "sort")
    @ApiModelProperty(value="Sorting index")
    private Integer sort;

    // Status 0-invalid 1-valid
    @TableField(value = "status")
    @ApiModelProperty(value="Status 0-invalid 1-valid")
    private Byte status;

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

    // The menu's privilege
    @TableField(exist = false)
    @ApiModelProperty("Menu's privilege")
    private List<SysPrivilege> privileges = Collections.emptyList();

    // Multiple submenus of one menu
    @TableField(exist = false)
    @ApiModelProperty("Submenus")
    private List<SysMenu> childs = Collections.emptyList();

    // Menu Key
    @TableField(exist = false)
    @ApiModelProperty("Menu Key")
    private  String menuKey ;

    // Get the menu id
    public String getMenuKey() {
        if (!StringUtils.isEmpty(parentKey)) {
            return parentKey+"."+id;
        } else {
            return id.toString();
        }
    }
}