package com.dsltyyz.bundle.jwt.token.annotation;

import java.lang.annotation.*;

/**
 * Description:
 * 检测token
 *
 * @author: dsltyyz
 * @date: 2020-9-8
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireToken {

    String[] value() default {};
}
