package com.dsltyyz.bundle.common.cache.client;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 默认缓存客户端
 *
 * @author: dsltyyz
 * @since: 2019-11-25
 */
@Slf4j
public class DefaultCacheClient implements CacheClient {

    private Map<String, Object> cacheMap = new HashMap<>();

    public DefaultCacheClient() {
        log.info("默认缓存客户端已加载");
    }

    @Override
    public <N> N getEntity(String key, TypeReference<N> typeReference) {
        Object o = cacheMap.get(key);
        if(o==null){
            return null;
        }
        return JSONObject.parseObject(o.toString(), typeReference);
    }

    @Override
    public <N> void putEntity(String key, N n) {
        cacheMap.put(key, n);
    }

    @Override
    public <N> void putEntity(String key, N n, Long expiredSeconds) {
        putEntity(key, n);
    }

    @Override
    public void deleteEntity(String key) {
        cacheMap.remove(key);
    }
}
