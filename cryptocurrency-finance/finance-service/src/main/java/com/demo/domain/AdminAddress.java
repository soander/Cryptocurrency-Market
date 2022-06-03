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

import javax.validation.constraints.NotNull;

@ApiModel(value="com-demo-domain-AdminAddress")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "admin_address")
public class AdminAddress {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Long id;

    @TableField(value = "coin_id")
    @ApiModelProperty(value="Coin id")
    @NotNull
    private Long coinId;

    @TableField(value = "keystore")
    @ApiModelProperty(value="eth keystore")
    private String keystore;

    @TableField(value = "pwd")
    @ApiModelProperty(value="eth password")
    private String pwd;

    @TableField(value = "address")
    @ApiModelProperty(value="Address")
    private String address;

    /**
     * 1:归账(冷钱包地址),2:打款,3:手续费
     */
    @TableField(value = "status")
    @ApiModelProperty(value="1:归账(冷钱包地址),2:打款,3:手续费")
    private Integer status;

    @TableField(value = "coin_type")
    @ApiModelProperty(value="Coin type")
//    @NotBlank
    private String coinType;
}