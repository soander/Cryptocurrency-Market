package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value="com-demo-domain-Market")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "market")
public class Market {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="Market id")
    private Long id;

    @TableField(value = "type")
    @ApiModelProperty(value="类型：1-数字货币；2：创新交易")
    private Byte type;

    @TableField(value = "trade_area_id")
    @ApiModelProperty(value="Trade area id")
    @NotNull
    private Long tradeAreaId;

    @TableField(value = "sell_coin_id")
    @ApiModelProperty(value="Sell coin id")
    @NotBlank
    private Long sellCoinId;

    @TableField(value = "buy_coin_id")
    @ApiModelProperty(value="Buy coin id")
    @NotNull
    private Long buyCoinId;

    @TableField(value = "symbol")
    @ApiModelProperty(value="Symbol")
    @NotNull
    private String symbol;

    @TableField(value = "name")
    @ApiModelProperty(value="Name")
    private String name;

    @TableField(value = "title")
    @ApiModelProperty(value="Title")
    private String title;

    @TableField(value = "img")
    @ApiModelProperty(value="Market img")
    private String img;

    @TableField(value = "open_price")
    @ApiModelProperty(value="Market open price")
    @NotNull
    private BigDecimal openPrice;

    @TableField(value = "fee_buy")
    @ApiModelProperty(value="Buy fee")
    @NotNull
    private BigDecimal feeBuy;

    @TableField(value = "fee_sell")
    @ApiModelProperty(value="Sell fee")
    @NotNull
    private BigDecimal feeSell;

    @TableField(value = "margin_rate")
    @ApiModelProperty(value="Margin rate")
    private BigDecimal marginRate;

    @TableField(value = "num_min")
    @ApiModelProperty(value="Minimum number")
    @NotNull
    private BigDecimal numMin;

    @TableField(value = "num_max")
    @ApiModelProperty(value="Maximum number")
    @NotNull
    private BigDecimal numMax;

    @TableField(value = "trade_min")
    @ApiModelProperty(value="Minimum trade number")
    private BigDecimal tradeMin;

    @TableField(value = "trade_max")
    @ApiModelProperty(value="Maximum trade number")
    private BigDecimal tradeMax;

    @TableField(value = "price_scale")
    @ApiModelProperty(value="Price scale")
    @NotNull
    private Byte priceScale;

    @TableField(value = "num_scale")
    @ApiModelProperty(value="Number scale")
    private Byte numScale;

    @TableField(value = "contract_unit")
    @ApiModelProperty(value="Contract unity")
    private Integer contractUnit;

    @TableField(value = "point_value")
    @ApiModelProperty(value="Point value")
    private BigDecimal pointValue;

    /**
     * 合并深度（格式：4,3,2）4:表示为0.0001；3：表示为0.001
     */
    @TableField(value = "merge_depth")
    @ApiModelProperty(value="Merge depth: 4-0.0001 3-0.001")
    @NotNull
    private String mergeDepth;

    @TableField(value = "trade_time")
    @ApiModelProperty(value="Trade time")
    @NotNull
    private String tradeTime;

    @TableField(value = "trade_week")
    @ApiModelProperty(value="Trade week")
    @NotNull
    private String tradeWeek;

    @TableField(value = "sort")
    @ApiModelProperty(value="Sort")
    private Integer sort;

    @TableField(value = "status")
    @ApiModelProperty(value="Status: 0-disable 1-enable")
    private Integer status;

    @TableField(value = "fxcm_symbol")
    @ApiModelProperty(value="FXCM API symbol")
    private String fxcmSymbol;

    @TableField(value = "yahoo_symbol")
    @ApiModelProperty(value="Yahoo API symbol")
    private String yahooSymbol;

    @TableField(value = "aliyun_symbol")
    @ApiModelProperty(value="Aliyun API symbol")
    private String aliyunSymbol;

    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="Last update time")
    private Date lastUpdateTime;

    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="Created time")
    private Date created;
}