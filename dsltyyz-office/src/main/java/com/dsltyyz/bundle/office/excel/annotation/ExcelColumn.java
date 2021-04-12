package com.dsltyyz.bundle.office.excel.annotation;

import java.lang.annotation.*;

/**
 * Description:
 * 导出EXCEL列
 *
 * @author: dsltyyz
 * @since: 2021-04-12
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    boolean value() default true;
}
