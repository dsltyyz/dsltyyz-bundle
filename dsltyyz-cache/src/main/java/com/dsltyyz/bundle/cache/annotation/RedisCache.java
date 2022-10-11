package com.dsltyyz.bundle.cache.annotation;

import com.dsltyyz.bundle.cache.enums.CacheType;

import java.lang.annotation.*;

/**
 * REDIS缓存
 * @author dsltyyz
 * @date 2022-10-11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {
    //缓存名称
    String cacheName();

    //支持springEl表达式
    String key();

    //REDIS超时时间（秒）
    long redisTimeOut() default 120;

    //缓存类型
    CacheType type() default CacheType.FULL;
}
