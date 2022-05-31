package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Register form data")
public class RegisterParam extends GeetestForm {

    @ApiModelProperty(value = "Country code")
    @NotBlank
    private String countryCode;

    @ApiModelProperty(value = "Email")
    private String email;

    @ApiModelProperty(value = "Phone number")
    private String mobile;

    @ApiModelProperty(value = "Invitation code")
    private String invitionCode;

    @ApiModelProperty(value = "Password")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "Verification code")
    private String validateCode;
}
