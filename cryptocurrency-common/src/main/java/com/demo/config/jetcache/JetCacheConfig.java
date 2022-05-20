package com.demo.config.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName JetCacheConfig
 * @Description The Jet cahce configuration
 * @Author Yaozheng Wang
 * @Date 2022/5/18 23:48
 **/
@Configuration
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.demo.service.impl")
public class JetCacheConfig {

}
