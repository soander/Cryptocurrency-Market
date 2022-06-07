package com.demo.config.feign;

import com.demo.constant.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
* @Author Yaozheng Wang
* @Description
* @Date 2022/5/29 16:21
* @Param
* @Return * @return null
**/
@Slf4j
public class OAuth2FeignConfig implements RequestInterceptor {

    // Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
    @Override
    public void apply(RequestTemplate template) {
        // Get token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String header;
        if (requestAttributes == null) {
            header = "bearer "+ Constants.INSIDE_TOKEN;
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            header = request.getHeader(HttpHeaders.AUTHORIZATION);
        }

        if (!StringUtils.isEmpty(header)) {
            template.header(HttpHeaders.AUTHORIZATION, header);
        }
    }
}
