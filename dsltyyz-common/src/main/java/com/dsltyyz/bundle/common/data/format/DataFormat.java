package com.dsltyyz.bundle.common.data.format;

import java.lang.annotation.*;

/**
 * 数据格式
 * @author dsltyyz
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFormat {
    /**
     * 处理类
     * @return
     */
    Class value();

    /**
     * 处理参数
     * @return
     */
    String param() default "";
}
