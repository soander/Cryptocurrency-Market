package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Update user password")
public class UpdateLoginParam {

    @ApiModelProperty(value = "Old password")
    @NotBlank
    private String oldpassword;

    @ApiModelProperty(value = "New password")
    @NotBlank
    private String newpassword;

    @ApiModelProperty(value = "Sms validate code")
    @NotBlank
    private String validateCode;
}
