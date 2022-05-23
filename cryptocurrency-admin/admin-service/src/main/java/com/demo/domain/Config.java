package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

// System configuration
@ApiModel(value="com-demo-domain-Config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "config")
public class Config {

    // Primary key
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="Primary key")
    private Long id;

    // Config rule type
    @TableField(value = "`type`")
    @ApiModelProperty(value="Config rule type")
    @NotBlank
    private String type;

    // Config rule code
    @TableField(value = "code")
    @ApiModelProperty(value="Config rule code")
    @NotBlank
    private String code;

    // Config rule name
    @TableField(value = "`name`")
    @ApiModelProperty(value="Config rule name")
    @NotBlank
    private String name;

    // Config rule description
    @TableField(value = "`desc`")
    @ApiModelProperty(value="Config rule description")
    private String desc;

    // Config value
    @TableField(value = "`value`")
    @ApiModelProperty(value="Config value")
    @NotBlank
    private String value;

    // Created time
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="Created time")
    private Date created;
}