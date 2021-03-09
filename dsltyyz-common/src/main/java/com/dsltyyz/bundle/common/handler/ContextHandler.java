package com.dsltyyz.bundle.common.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * 线程存储数据
 *
 * @author: dsltyyz
 * @since: 2019-3-20
 */
public class ContextHandler {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置键值对
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        map.put(key, value);
    }

    /**
     * 根据键获取值
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map.get(key);
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public static Object remove(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map.remove(key);
    }

    /**
     * 清除
     */
    public static void clear() {
        THREAD_LOCAL.get().clear();
    }
}
