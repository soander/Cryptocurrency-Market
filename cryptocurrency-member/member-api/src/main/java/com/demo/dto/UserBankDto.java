package com.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "User bank dto")
public class UserBankDto {

    @ApiModelProperty(value="User real name")
    private String realName;

    @ApiModelProperty(value="Bank name")
    private String bank;

    @ApiModelProperty(value="Bank province")
    private String bankProv;

    @ApiModelProperty(value="Bank city")
    private String bankCity;

    @ApiModelProperty(value="Bank address")
    private String bankAddr;

    @ApiModelProperty(value="Bank card number")
    private String bankCard;
}
