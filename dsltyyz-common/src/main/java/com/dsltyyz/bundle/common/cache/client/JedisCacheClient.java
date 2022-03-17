package com.dsltyyz.bundle.common.cache.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Jedis缓存客户端
 *
 * @author: dsltyyz
 * @since: 2019-11-25
 */
@Slf4j
public class JedisCacheClient implements CacheClient {

    @Resource
    private RedisTemplate redisTemplate;

    public JedisCacheClient() {
        log.info("redis缓存客户端已加载");
    }

    @Override
    public <N> N getEntity(String key, TypeReference<N> typeReference) {
        Object o = redisTemplate.opsForValue().get(key);
        if(o==null){
            return null;
        }
        return JSONObject.parseObject(o.toString(), typeReference);

    }

    @Override
    public <N> void putEntity(String key, N n) {
        //反射获取对象的Class
        redisTemplate.opsForValue().set(key, JSON.toJSONString(n));
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
