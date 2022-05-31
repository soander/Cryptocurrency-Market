package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Set Pay Password Param")
public class UnsetPayPasswordParam {

    @ApiModelProperty(value = "New Pay Password")
    @NotBlank
    private String payPassword;

    @ApiModelProperty(value = "Sms Code")
    @NotBlank
    private String validateCode;
}
