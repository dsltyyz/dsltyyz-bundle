package com.dsltyyz.bundle.common.cache.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Jedis缓存客户端
 *
 * @author: dsltyyz
 * @date: 2019-11-25
 */
@Slf4j
public class JedisCacheClient implements CacheClient {

    @Resource
    private RedisTemplate redisTemplate;

    public JedisCacheClient() {
        log.info("redis缓存客户端已加载");
    }

    @Override
    public <N> N getEntity(String key, Class<N> nClass) {
        return (N)redisTemplate.opsForValue().get(key);

    }

    @Override
    public <N> void putEntity(String key, N n) {
        redisTemplate.opsForValue().set(key, n);
    }

    @Override
    public <N> void putEntity(String key, N n, Long expiredSeconds) {
        redisTemplate.opsForValue().set(key, n, expiredSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void deleteEntity(String key) {
        redisTemplate.delete(key);
    }
}
