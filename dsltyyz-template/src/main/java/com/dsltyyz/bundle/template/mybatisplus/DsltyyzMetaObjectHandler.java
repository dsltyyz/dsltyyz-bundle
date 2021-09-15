package com.dsltyyz.bundle.template.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * Mybatis自动填充
 *
 * @author dsltyyz
 */
public class DsltyyzMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //创建时间
        boolean createTimeHasSetter = metaObject.hasSetter("createTime");
        //是否存在这个属性方法
        if (createTimeHasSetter) {
            Object createTime = getFieldValByName("createTime", metaObject);
            //是否已经赋值
            if (createTime == null) {
                strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            }
        }

        //更新时间
        boolean updateTimeHasSetter = metaObject.hasSetter("updateTime");
        //是否存在这个属性方法
        if (updateTimeHasSetter) {
            Object updateTime = getFieldValByName("updateTime", metaObject);
            //是否已经赋值
            if (updateTime == null) {
                strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        }

        //是否删除
        boolean deletedHasSetter = metaObject.hasSetter("deleted");
        //是否存在这个属性方法
        if (deletedHasSetter) {
            strictInsertFill(metaObject, "deleted", Integer.class, new Integer(0));
        }

        //乐观锁
        boolean versionHasSetter = metaObject.hasSetter("version");
        //是否存在这个属性方法
        if (versionHasSetter) {
            strictInsertFill(metaObject, "version", Integer.class, new Integer(1));
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean updateTimeHasSetter = metaObject.hasSetter("updateTime");
        //是否存在这个属性方法
        if (updateTimeHasSetter) {
            //强行更新
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
            //如下方法 在updateTime不为空时不填充
            //strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        }
    }
}