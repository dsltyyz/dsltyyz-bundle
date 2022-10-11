package com.dsltyyz.bundle.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine缓存配置
 * @author dsltyyz
 * @date 2022-10-11
 */
@Configuration
public class CaffeineCacheManagerConfig {

    @Value("${caffeine.timeout:60}")
    private long timeout;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(128)
                .maximumSize(1024)
                .expireAfterWrite(timeout, TimeUnit.SECONDS));
        return cacheManager;
    }
}
