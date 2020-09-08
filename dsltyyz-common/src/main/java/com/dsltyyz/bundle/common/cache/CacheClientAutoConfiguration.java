package com.dsltyyz.bundle.common.cache;

import com.dsltyyz.bundle.common.cache.client.DefaultCacheClient;
import com.dsltyyz.bundle.common.cache.client.JedisCacheClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * 自动加载缓存客户端
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@Configuration
public class CacheClientAutoConfiguration {

    @ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true")
    @Bean("cacheClient")
    public JedisCacheClient jedisCacheClient(){
        return new JedisCacheClient();
    }

    @ConditionalOnMissingBean(name = {"cacheClient"})
    @Bean("cacheClient")
    public DefaultCacheClient defaultCacheClient(){
        return new DefaultCacheClient();
    }

}
