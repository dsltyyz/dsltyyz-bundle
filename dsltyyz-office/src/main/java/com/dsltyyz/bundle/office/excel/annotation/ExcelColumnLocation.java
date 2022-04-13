package com.dsltyyz.bundle.office.excel.annotation;

import com.dsltyyz.bundle.office.excel.handler.DataHandler;
import com.dsltyyz.bundle.office.excel.handler.DefaultDataHandler;

import java.lang.annotation.*;

/**
 * EXCEL字段定位
 * @author dsltyyz
 * @date 2022-4-13
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumnLocation {

    //列坐标
    int value();

    Class<? extends DataHandler> dataHandler() default DefaultDataHandler.class;

    //处理类型
    String pattern() default "";
}
