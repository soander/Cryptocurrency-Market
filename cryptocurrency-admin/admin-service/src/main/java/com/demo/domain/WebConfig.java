package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

// Website configuration
@ApiModel(value="com-demo-domain-WebConfig")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "web_config")
public class WebConfig {

    // Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Id")
    private Long id;

    // Group: LINK_BANNER ,WEB_BANNER
    @TableField(value = "type")
    @ApiModelProperty(value="Group: LINK_BANNER ,WEB_BANNER")
    @NotBlank
    private String type;

    // Name
    @TableField(value = "name")
    @ApiModelProperty(value="Name")
    @NotBlank
    private String name;

    // Value
    @TableField(value = "value")
    @ApiModelProperty(value="Value")
    @NotBlank
    private String value;

    // Sort 权重
    @TableField(value = "sort")
    @ApiModelProperty(value="权重")
    private Short sort;

    // Created time
    @TableField(value = "created" ,fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date created;

    // URL
    @TableField(value = "url")
    @ApiModelProperty(value="url")
    private String url;

    // Status: 0-No use 1-used
    @TableField(value = "status")
    @ApiModelProperty(value="Status: 0-No use 1-used")
    private Integer status;
}