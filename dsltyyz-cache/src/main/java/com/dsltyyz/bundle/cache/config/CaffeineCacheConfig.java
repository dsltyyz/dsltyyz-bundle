package com.dsltyyz.bundle.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine缓存配置
 * @author dsltyyz
 * @date 2022-10-11
 */
@Configuration
public class CaffeineCacheConfig {

    @Value("${lettuce.timeout:60}")
    private long timeout;

    @Bean
    public Cache<String,Object> caffeineCache(){
        return Caffeine.newBuilder()
                //初始大小
                .initialCapacity(128)
                //最大数量
                .maximumSize(1024)
                //过期时间
                .expireAfterWrite(timeout, TimeUnit.SECONDS)
                .build();
    }
}
