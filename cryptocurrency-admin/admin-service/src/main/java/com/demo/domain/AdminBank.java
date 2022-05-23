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

// Bank management
@ApiModel(value="com-demo-domain-AdminBank")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "admin_bank")
public class AdminBank {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Primary key")
    private Long id;

    // Account holder name
    @TableField(value = "name")
    @ApiModelProperty(value="Account holder name")
    @NotBlank
    private String name;

    // Bank name
    @TableField(value = "bank_name")
    @ApiModelProperty(value="Bank name")
    @NotBlank
    private String bankName;

    // Bank card
    @TableField(value = "bank_card")
    @ApiModelProperty(value="Bank card")
    @NotBlank
    private String bankCard;

    // Buy coin id
    @TableField(value = "coin_id")
    @ApiModelProperty(value="Coin id")
    private Long coinId;

    // Coin name
    @TableField(value = "coin_name")
    @ApiModelProperty(value="Coin name")
    private String coinName;

    // Status: 0-invalid 1-valid
    @TableField(value = "status")
    @ApiModelProperty(value="Status: 0-invalid 1-valid")
    private Byte status;
}