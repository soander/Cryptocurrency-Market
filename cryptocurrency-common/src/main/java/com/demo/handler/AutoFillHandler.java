package com.demo.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AutoFillHandler implements MetaObjectHandler {

    // Fill when inserting
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = getCurrentUserId();
        this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        this.strictInsertFill(metaObject, "created", Date.class, new Date());
        this.strictInsertFill(metaObject, "lastUpdateTime", Date.class, new Date());
    }

    // Fill when updating
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = getCurrentUserId();

        this.strictUpdateFill(metaObject, "modifyBy", Long.class, userId);
        this.strictUpdateFill(metaObject, "lastUpdateTime", Date.class, new Date());
    }

    // Get current user
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null) {
            String s = authentication.getPrincipal().toString();
            if("anonymousUser".equals(s)) {
                return null;
            }
            return Long.valueOf(s);
        }
        return null;
    }
}
