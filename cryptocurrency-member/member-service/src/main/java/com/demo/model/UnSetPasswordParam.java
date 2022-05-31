package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "SetPasswordParam")
public class UnSetPasswordParam extends GeetestForm {

    @ApiModelProperty(value = "Country code")
    @NotBlank
    private  String countryCode;

    @ApiModelProperty(value = "phone number")
    @NotBlank
    private String mobile;

    @ApiModelProperty(value = "New password")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "validate code")
    @NotBlank
    private String validateCode;
}
