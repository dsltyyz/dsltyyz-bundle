package com.dsltyyz.bundle.common.token.annotation;

import java.lang.annotation.*;

/**
 * Description:
 * 检测授权
 *
 * @author: dsltyyz
 * @date: 2020-9-8
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAuthorization {
}
