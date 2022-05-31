package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Login form")
public class LoginForm extends GeetestForm {

    @ApiModelProperty(value = "Country phone code")
    private String countryCode;

    @ApiModelProperty(value = "User name")
    @NotBlank
    private String username;

    @ApiModelProperty(value = "User password")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "User's uuid")
    private String uuid;
}
