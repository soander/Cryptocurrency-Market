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

@ApiModel(value="com-demo-domain-UserFavoriteMarket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_favorite_market")
public class UserFavoriteMarket {

    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="Primary Key")
    private Long id;

    @TableField(value = "type")
    @ApiModelProperty(value="交易对类型：1-币币交易；2-创新交易；")
    private Integer type;

    @TableField(value = "user_id")
    @ApiModelProperty(value="User id")
    private Long userId;

    @TableField(value = "market_id")
    @ApiModelProperty(value="Market id")
    private Long marketId;
}