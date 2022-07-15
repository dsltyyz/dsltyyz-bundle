package com.dsltyyz.bundle.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * Description:
 * YAML工具类
 *
 * @author: dsltyyz
 * @date 2022-3-30
 */
public class YamlUtils {

    /**
     * 获取对象
     *
     * @param inputStream
     * @param typeReference
     * @return
     */
    public static <T> T getObject(InputStream inputStream, TypeReference<T> typeReference) {
        Object object = getObject(inputStream);
        return JSONObject.parseObject(JSONObject.toJSONString(object), typeReference);
    }

    /**
     * 通过流获取
     *
     * @param inputStream
     * @return
     */
    public static Object getObject(InputStream inputStream) {
        Yaml yaml = new Yaml();
        return yaml.load(inputStream);
    }

}
