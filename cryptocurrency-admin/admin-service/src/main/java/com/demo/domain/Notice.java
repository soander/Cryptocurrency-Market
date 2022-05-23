package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

// System Notice
@ApiModel(value="com-demo-domain-Notice")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "notice")
public class Notice {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    // Notice title
    @TableField(value = "title")
    @ApiModelProperty(value="title")
    @NotBlank
    private String title;

    // Notice description
    @TableField(value = "description")
    @ApiModelProperty(value="description")
    @NotBlank
    private String description;

    // Notice Author
    @TableField(value = "author")
    @ApiModelProperty(value="Author")
    @NotBlank
    private String author;

    // Notice status
    @TableField(value = "status")
    @ApiModelProperty(value="status")
    private Integer status;

    // Notice sorting
    @TableField(value = "sort")
    @ApiModelProperty(value="sort")
    @NotNull
    private Integer sort;

    // Notice content
    @TableField(value = "content")
    @ApiModelProperty(value="content")
    @NotBlank
    private String content;

    // Notice last update time
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="last update time")
    private Date lastUpdateTime;

    // Notice created time
    @TableField(value = "created" ,fill = FieldFill.INSERT)
    @ApiModelProperty(value="created time")
    private Date created;
}