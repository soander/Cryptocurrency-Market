package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * {"countryCode":"+86","newMobilePhone":"15827293118","validateCode":"111111","oldValidateCode":"111111"}
 */
@Data
@ApiModel(value = "Update phone number")
public class UpdatePhoneParam {

    @ApiModelProperty(value = "Country code")
    @NotBlank
    private String countryCode;

    @ApiModelProperty(value = "New phone number")
    @NotBlank
    private String newMobilePhone;

    @ApiModelProperty(value = "New phone validate code")
    @NotBlank
    private String validateCode;

    @ApiModelProperty(value = "Old phone validate code")
    @NotBlank
    private String oldValidateCode;
}
