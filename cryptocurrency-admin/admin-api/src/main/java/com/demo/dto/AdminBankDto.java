package com.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AdminBankDto
 * @Description Admin bank dto
 * @Author Yaozheng Wang
 * @Date 2022/6/2 13:22
 **/
@Data
@ApiModel(value = "Bank card parameters")
public class AdminBankDto {

    @ApiModelProperty(value = "Bank account user name")
    private String name;

    @ApiModelProperty(value = "Bank name")
    private String bankName;

    @ApiModelProperty(value = "Bank card number")
    private String bankCard;
}
