package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@ApiModel(value="com-demo-domain-TradeArea")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "trade_area")
public class TradeArea {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Primary Key")
    private Long id;

    @TableField(value = "name")
    @ApiModelProperty(value="Trade area name")
    @NotBlank
    private String name;

    @TableField(value = "code")
    @ApiModelProperty(value="Trade area code")
    @NotBlank
    private String code;

    /**
     * 类型：1-数字货币交易；2-创新交易使用；
     */
    @TableField(value = "type")
    @ApiModelProperty(value="Type: 1-数字货币交易；2-创新交易使用；")
    private Byte type;

    @TableField(value = "coin_id")
    @ApiModelProperty(value="Coin id")
    private Long coinId;

    @TableField(value = "coin_name")
    @ApiModelProperty(value="Coin name")
    private String coinName;

    @TableField(value = "sort")
    @ApiModelProperty(value="Sort")
    private Byte sort;

    @TableField(value = "status")
    @ApiModelProperty(value="Status")
    private Byte status;

    @TableField(value = "base_coin")
    @ApiModelProperty(value="Base coin: 0-No; 1-Yes")
    private Long baseCoin;

    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="Last update time")
    private Date lastUpdateTime;

    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="Created time")
    private Date created;
}