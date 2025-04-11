package com.rs.handler.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.rs.uitils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

//    LocalDateTime now = LocalDateTime.now();
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId = -1L; // 表示是自己创建
        }

        // 填充创建时间、创建者、更新时间、更新者
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", () -> now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", () -> now, LocalDateTime.class);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充更新时间和更新者
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "updateTime", () -> now, LocalDateTime.class);
        this.setFieldValByName("updateBy", SecurityUtils.getUserId(), metaObject);
    }
}
