package com.demo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "User Dto object, solve the huge data transfer problem")
public class UserDto {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Long id;

    @TableField(value = "username")
    @ApiModelProperty(value="User name")
    private String username;

    @TableField(value = "country_code")
    @ApiModelProperty(value="Country code")
    private String countryCode;

    @TableField(value = "mobile")
    @ApiModelProperty(value="Mobile phone number")
    private String mobile;

    @TableField(value = "email")
    @ApiModelProperty(value="Email")
    private String email;

    @TableField(value = "real_name")
    @ApiModelProperty(value="Real name")
    private String realName;

    @ApiModelProperty(value="Payment password")
    private String paypassword ;
}
