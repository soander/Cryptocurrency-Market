package com.demo.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName SwaggerProperties
 * @Description Swagger Properties
 * @Author Yaozheng Wang
 * @Date 2022/5/19 0:13
 **/
@Data
@ConfigurationProperties(prefix = "swagger2")
public class SwaggerProperties {

    private String basePackage;

    private String name;

    private String url;

    private String email;

    private String  title;

    private String description;

    private String version;

    private String termsOfServiceUrl;
}