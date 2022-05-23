package com.demo.aspect;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.demo.domain.SysUserLog;
import com.demo.model.WebLog;
import com.demo.service.SysUserLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WebLogAdminAspect
 * @Description The web log admin aspect
 * @Author Yaozheng Wang
 * @Date 2022/5/20 21:38
 **/
@Aspect
@Order(2)
@Slf4j
public class WebLogAdminAspect {

    private Snowflake snowflake = new Snowflake(1,1);

    @Autowired
    private SysUserLogService sysUserLogService;

    @Pointcut("execution(* com.demo.controller.*.*(..))")
    public void webLog(){}

    @Around("webLog()")
    public Object recodeWebLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;
        WebLog webLog = new WebLog();
        long start = System.currentTimeMillis();
        result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());

        long end = System.currentTimeMillis() ;
        webLog.setSpendTime((int)(start-end)/1000);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String url = request.getRequestURL().toString();
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(url);
        webLog.setBasePath(StrUtil.removeSuffix(url, URLUtil.url(url).getPath()));
        webLog.setUsername(authentication==null ? "anonymous":authentication.getPrincipal().toString());
        webLog.setIp(request.getRemoteAddr());

        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();

        String targetClassName = proceedingJoinPoint.getTarget().getClass().getName();
        Method method = signature.getMethod();

        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        webLog.setDescription(annotation==null ? "no desc":annotation.value());
        webLog.setMethod(targetClassName+"."+method.getName());
        webLog.setParameter(getMethodParameter(method,proceedingJoinPoint.getArgs()));
        webLog.setResult(result);

        SysUserLog sysUserLog = new SysUserLog();

        sysUserLog.setId(snowflake.nextId());
        sysUserLog.setCreated(new Date());
        sysUserLog.setDescription(webLog.getDescription());
        sysUserLog.setGroup(webLog.getDescription());
        sysUserLog.setUserId(Long.valueOf(webLog.getUsername()));
        sysUserLog.setMethod(webLog.getMethod());
        sysUserLog.setIp(sysUserLog.getIp());
        sysUserLogService.save(sysUserLog);
        return result ;
    }

    private Object getMethodParameter(Method method, Object[] args) {
        Map<String, Object> methodParametersWithValues = new HashMap<>();
        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer =
                new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i <parameterNames.length ; i++) {
            if(parameterNames[i].equals("password") || parameterNames[i].equals("file")) {
                methodParametersWithValues.put(parameterNames[i],"受限的支持类型");
            } else {
                methodParametersWithValues.put(parameterNames[i],args[i]);
            }
        }
        return methodParametersWithValues;
    }
}
