package com.dsltyyz.bundle.jwt.token.annotation;

import java.lang.annotation.*;

/**
 * Description:
 * 检测admin token
 *
 * @author: dsltyyz
 * @date: 2020-9-8
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdminToken {

    String[] value() default {};
}
