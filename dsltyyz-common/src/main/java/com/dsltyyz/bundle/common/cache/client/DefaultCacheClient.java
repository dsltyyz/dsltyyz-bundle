package com.dsltyyz.bundle.common.cache.client;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 默认缓存客户端
 *
 * @author: dsltyyz
 * @date: 2019/11/25
 */
@Slf4j
public class DefaultCacheClient implements CacheClient {

    private Map<String,Object> cacheMap = new HashMap<>();

    @Override
    public void getInfo() {
        System.out.println("默认缓存客户端已加载");
    }

    @Override
    public <N> N getEntity(String key, Class<N> clazz) {
        return (N)cacheMap.get(key);
    }

    @Override
    public <N> void putEntity(String key, N n) {
        cacheMap.put(key, n);
    }

    @Override
    public void deleteEntity(String key) {
        cacheMap.remove(key);
    }
}