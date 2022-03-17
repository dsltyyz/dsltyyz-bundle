package com.dsltyyz.bundle.common.cache.client;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
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
    public <N> N getEntity(String key, Class<N> clazz) {
        //根据key获取value
        byte[] bytes = redisTemplate.getConnectionFactory().getConnection().get(key.getBytes());
        //value不为空
        if (null != bytes) {
            //protostuff根据Class类获取当前实体对象
            RuntimeSchema<N> schema = RuntimeSchema.createFrom(clazz);
            N n = schema.newMessage();
            //数据组装进对象
            ProtostuffIOUtil.mergeFrom(bytes, n, schema);
            return n;
        }
        return null;
    }

    @Override
    public <N> void putEntity(String key, N n) {
        //反射获取对象的Class
        Class clazz = n.getClass();
        //protostuff根据Class类获取当前实体对象
        RuntimeSchema<N> schema = RuntimeSchema.createFrom(clazz);
        byte[] bytes = ProtostuffIOUtil.toByteArray(n, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        redisTemplate.opsForValue().set(key.getBytes(), bytes);
    }

    @Override
    public <N> void putEntity(String key, N n, Long expiredSeconds) {
        //反射获取对象的Class
        Class clazz = n.getClass();
        //protostuff根据Class类获取当前实体对象
        RuntimeSchema<N> schema = RuntimeSchema.createFrom(clazz);
        byte[] bytes = ProtostuffIOUtil.toByteArray(n, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        redisTemplate.opsForValue().set(key.getBytes(), bytes, expiredSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void deleteEntity(String key) {
        redisTemplate.delete(key.getBytes());
    }
}
