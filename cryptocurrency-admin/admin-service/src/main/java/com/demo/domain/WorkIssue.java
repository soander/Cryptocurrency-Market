package com.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Work issue
@ApiModel(value="com-demo-domain-WorkIssue")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "work_issue")
public class WorkIssue {

    // Primary key
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    // User id
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户id(提问用户id)")
    private Long userId;

    // Worker id
    @TableField(value = "answer_user_id")
    @ApiModelProperty(value="Worker id")
    private Long answerUserId;

    // Worker name
    @TableField(value = "answer_name")
    @ApiModelProperty(value="Worker name")
    private String answerName;

    // Question content
    @TableField(value = "question")
    @ApiModelProperty(value="Question content")
    private String question;

    // Question answer
    @TableField(value = "answer")
    @ApiModelProperty(value="Question answer")
    private String answer;

    // Status: 1-waiting answer 2-answered
    @TableField(value = "status")
    @ApiModelProperty(value="Status: 1-waiting answer 2-answered")
    private Integer status;

    // Updated time
    @TableField(value = "last_update_time" ,fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="Updated time")
    private Date lastUpdateTime;

    // Created time
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="Created time")
    private Date created;

    // User name
    @TableField(exist = false)
    @ApiModelProperty(value = "Work issues' user name")
    public String username ="测试用户" ;

    // User name
    @TableField(exist = false)
    @ApiModelProperty(value = "Work issues' real name")
    private String realName= "测试用户" ;
}