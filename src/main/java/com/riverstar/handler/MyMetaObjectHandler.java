package com.riverstar.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.riverstar.tool.DateTool;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"createTime",Integer.class,DateTool.currentTime());
        this.strictInsertFill(metaObject,"updateTime",Integer.class,DateTool.currentTime());
        this.strictInsertFill(metaObject,"deleteFlag",Integer.class,0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime",Integer.class,DateTool.currentTime());
    }


}
