package com.dsltyyz.bundle.cache.aop;

import com.dsltyyz.bundle.cache.annotation.CaffeineRedisCache;
import com.dsltyyz.bundle.cache.enums.CacheType;
import com.dsltyyz.bundle.common.util.ElUitls;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * CaffeineRedisCache切面
 *
 * @author dsltyyz
 * @date 2022-10-11
 */
@Slf4j
@Component
@Aspect
public class CaffeineRedisCacheAspect {

    @Resource
    private Cache cache;

    @Resource
    private RedisTemplate redisTemplate;

    @Around("@annotation(caffeineRedisCache)")
    public Object doAround(ProceedingJoinPoint point, CaffeineRedisCache caffeineRedisCache) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();

        //拼接解析springEl表达式的map
        String[] paramNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            treeMap.put(paramNames[i], args[i]);
        }

        String elResult = ElUitls.parse(caffeineRedisCache.key(), treeMap);
        String realKey = String.format("{%s}_{%s}_{%s}", caffeineRedisCache.cacheName(), caffeineRedisCache.key(), elResult);

        //强制更新
        if (caffeineRedisCache.type() == CacheType.PUT) {
            Object object = point.proceed();
            redisTemplate.opsForValue().set(realKey, object, caffeineRedisCache.redisTimeOut(), TimeUnit.SECONDS);
            cache.put(realKey, object);
            return object;
        }//删除
        else if (caffeineRedisCache.type() == CacheType.DELETE) {
            redisTemplate.delete(realKey);
            cache.invalidate(realKey);
            return point.proceed();
        }


        //读写，查询Caffeine
        Object caffeineCacheObject = cache.getIfPresent(realKey);
        if (Objects.nonNull(caffeineCacheObject)) {
            log.info("get data from caffeine");
            return caffeineCacheObject;
        }

        //查询Redis
        Object redisCacheObject = redisTemplate.opsForValue().get(realKey);
        if (Objects.nonNull(redisCacheObject)) {
            log.info("get data from redis");
            cache.put(realKey, redisCacheObject);
            return redisCacheObject;
        }

        log.info("get data from database");
        Object object = point.proceed();
        if (Objects.nonNull(object)) {
            //写入Redis
            redisTemplate.opsForValue().set(realKey, object, caffeineRedisCache.redisTimeOut(), TimeUnit.SECONDS);
            //写入Caffeine
            cache.put(realKey, object);
        }
        return object;
    }

}
