package com.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value="com-demo-domain-UserWallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_wallet")
public class UserWallet {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    @TableField(value = "user_id")
    @ApiModelProperty(value="User id")
    private Long userId;

    @TableField(value = "coin_id")
    @ApiModelProperty(value="Coin id")
    @NotNull
    private Long coinId;

    @TableField(value = "coin_name")
    @ApiModelProperty(value="Coin name")
    private String coinName;

    @TableField(value = "name")
    @ApiModelProperty(value="Coin address name")
    @NotBlank
    private String name;

    @TableField(value = "addr")
    @ApiModelProperty(value="Coin address")
    @NotBlank
    private String addr;

    @TableField(value = "sort")
    @ApiModelProperty(value="Sort")
    private Integer sort;

    @TableField(value = "status")
    @ApiModelProperty(value="Status")
    private Byte status;

    @TableField(value = "last_update_time")
    @ApiModelProperty(value="Last update time")
    private Date lastUpdateTime;

    @TableField(value = "created")
    @ApiModelProperty(value="Created time")
    private Date created;

    @TableField(exist = false)
    @ApiModelProperty(value = "Pay password")
    @NotBlank
    private String payPassword;
}