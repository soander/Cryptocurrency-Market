package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

// Sms information
@ApiModel(value="com-demo-domain-Sms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sms")
public class Sms {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Primary key")
    private Long id;

    // Template code
    @TableField(value = "template_code")
    @ApiModelProperty(value="Template code")
    @NotBlank
    private String templateCode;

    // Country code
    @TableField(value = "country_code")
    @ApiModelProperty(value="Country code")
    @NotBlank
    private String countryCode;

    // Phone number
    @TableField(value = "mobile")
    @ApiModelProperty(value="Phone number")
    @NotBlank
    private String mobile;

    // Sms content
    @TableField(value = "content")
    @ApiModelProperty(value="Sms content")
    private String content;

    // Sms status
    @TableField(value = "status")
    @ApiModelProperty(value="Sms status：0: default; >0: Sms number；<0: invalid；")
    private Integer status;

    // Remark
    @TableField(value = "remark")
    @ApiModelProperty(value="Remark")
    private String remark;

    // Send time
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="Send time")
    private Date lastUpdateTime;

    // Create time
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="Create time")
    private Date created;
}