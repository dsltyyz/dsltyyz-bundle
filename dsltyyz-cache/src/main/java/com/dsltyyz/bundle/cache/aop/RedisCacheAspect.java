package com.dsltyyz.bundle.cache.aop;

import com.dsltyyz.bundle.cache.annotation.RedisCache;
import com.dsltyyz.bundle.cache.enums.CacheType;
import com.dsltyyz.bundle.common.util.ElUitls;
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
public class RedisCacheAspect {

    @Resource
    private RedisTemplate redisTemplate;

    @Around("@annotation(redisCache)")
    public Object doAround(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();

        //拼接解析springEl表达式的map
        String[] paramNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            treeMap.put(paramNames[i], args[i]);
        }

        String elResult = ElUitls.parse(redisCache.key(), treeMap);
        String realKey = String.format("%s_%s_%s", redisCache.cacheName(), redisCache.key(), elResult);

        //强制更新
        if (redisCache.type() == CacheType.PUT) {
            Object object = point.proceed();
            redisTemplate.opsForValue().set(realKey, object, redisCache.redisTimeOut(), TimeUnit.SECONDS);
            return object;
        }//删除
        else if (redisCache.type() == CacheType.DELETE) {
            redisTemplate.delete(realKey);
            return point.proceed();
        }

        //查询Redis
        Object redisCacheObject = redisTemplate.opsForValue().get(realKey);
        if (Objects.nonNull(redisCacheObject)) {
            log.info("{} get data from redis", realKey);
            return redisCacheObject;
        }

        Object object = point.proceed();
        log.info("{} get data from database", realKey);
        if (Objects.nonNull(object)) {
            //写入Redis
            redisTemplate.opsForValue().set(realKey, object, redisCache.redisTimeOut(), TimeUnit.SECONDS);
        }
        return object;
    }

}
