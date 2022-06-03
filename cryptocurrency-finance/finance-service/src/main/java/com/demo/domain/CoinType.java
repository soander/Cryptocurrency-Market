package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

// Coin Type
@ApiModel(value = "com-demo-domain-CoinType")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coin_type")
public class CoinType {

    // Primary Key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Primary Key")
    private Long id;

    // Coin Type Code
    @TableField(value = "code")
    @ApiModelProperty(value = "Coin Type Code")
    @NotBlank
    private String code;

    // Coin Description
    @TableField(value = "description")
    @ApiModelProperty(value = "Coin Description")
    private String description;

    // Coin Status
    @TableField(value = "status")
    @ApiModelProperty(value = "Coin Status: 0-invalid 1-valid")
    @NotNull
    private Byte status;

    // Coin create time
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "Coin create time")
    private Date created;

    // Coin last update time
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "Coin last update time")
    private Date lastUpdateTime;
}