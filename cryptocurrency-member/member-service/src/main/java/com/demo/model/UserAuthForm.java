package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "User identity Authorization")
public class UserAuthForm extends GeetestForm {

    @NotBlank
    @ApiModelProperty(value = "User real name")
    private String realName;

    @NotNull
    @ApiModelProperty(value = "Id card type")
    private Integer idCardType;

    @NotBlank
    @ApiModelProperty(value = "Id card number")
    private String idCard;
}
