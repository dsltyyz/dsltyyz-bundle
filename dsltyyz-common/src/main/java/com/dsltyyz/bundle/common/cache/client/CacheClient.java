package com.dsltyyz.bundle.common.cache.client;

/**
 * Description:
 * 缓存客户端
 *
 * @author: dsltyyz
 * @since: 2019-11-25
 */
public interface CacheClient {

    /**
     * 获取key实体
     *
     * @param key
     * @param clazz
     * @return
     */
    <N> N getEntity(String key, Class<N> clazz);

    /**
     * 添加key实体
     *
     * @param key
     * @param n
     */
    <N> void putEntity(String key, N n);

    /**
     * 删除key实体
     *
     * @param key
     */
    void deleteEntity(String key);
}
