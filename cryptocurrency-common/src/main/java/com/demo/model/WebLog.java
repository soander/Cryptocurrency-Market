package com.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName WebLog
 * @Description The common object
 * @Author Yaozheng Wang
 * @Date 2022/5/19 0:26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class WebLog {

    private String description;

    private String username;

    private Integer spendTime;

    private String basePath;

    private String uri;

    private String url;

    private String method;

    private String ip;

    private Object parameter;

    private Object result;
}
