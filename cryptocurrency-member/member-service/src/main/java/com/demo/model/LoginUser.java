package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "Login success user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    @ApiModelProperty(value = "User name")
    private String username;

    @ApiModelProperty(value = "Token expired time")
    private Long expire;

    @ApiModelProperty(value = "access_token")
    private String access_token;

    @ApiModelProperty(value = "refreshToken")
    private String refresh_token;
}
