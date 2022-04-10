package com.dsltyyz.bundle.common.cache.client;

/**
 * Description:
 * 缓存客户端
 *
 * @author: dsltyyz
 * @date: 2019-11-25
 */
public interface CacheClient {

    /**
     * 获取key实体
     *
     * @param key
     * @param nClass
     * @return
     */
    <N> N getEntity(String key, Class<N> nClass);

    /**
     * 添加key实体
     *
     * @param key
     * @param n
     */
    <N> void putEntity(String key, N n);

    /**
     * 添加key实体 并设置过期时间
     *
     * @param key
     * @param n
     * @param expiredSeconds
     */
    <N> void putEntity(String key, N n, Long expiredSeconds);

    /**
     * 删除key实体
     *
     * @param key
     */
    void deleteEntity(String key);
}
