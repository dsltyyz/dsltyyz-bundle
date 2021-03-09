package com.dsltyyz.bundle.common.util;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.lang.Assert;

import java.util.Properties;

/**
 * Description:
 * Mybatis Plus 加密及解密工具
 *
 * @author: dsltyyz
 * @date: 2021-03-09
 */
public class MpwUtils {

    private static String MPW_PREFIX = "mpw:";

    /**
     * 加密MPW
     *
     * @param properties
     * @return
     */
    public static Properties encrypt(Properties properties) {
        return encrypt(properties, AES.generateRandomKey());
    }

    /**
     * 加密MPW
     *
     * @param properties
     * @param key
     * @return
     */
    public static Properties encrypt(Properties properties, String key) {
        Properties mpwProperties = new Properties();
        Assert.notNull(key, "不能为空");
        Assert.isTrue(key.length() == 16, "key长度为16");
        System.out.println("MPW加密");
        System.out.println("--mpw.key=" + key);
        properties.forEach((k, v) -> {
            if (StringUtils.isNotBlank(v.toString())) {
                String value = AES.encrypt(v.toString(), key);
                System.out.println(k + ": " + MPW_PREFIX + value);
                mpwProperties.put(k, MPW_PREFIX + value);
            }else{
                System.out.println(k + ": " + v);
                mpwProperties.put(k, v);
            }

        });
        return mpwProperties;
    }

    /**
     * 解密MPW
     *
     * @param mpwProperties
     * @param key
     * @return
     */
    public static Properties decrypt(Properties mpwProperties, String key) {
        Properties properties = new Properties();
        Assert.notNull(key, "不能为空");
        Assert.isTrue(key.length() == 16, "key长度为16");
        mpwProperties.forEach((k, v) -> {
            if (StringUtils.isNotBlank(v.toString()) && v.toString().startsWith(MPW_PREFIX)) {
                String value = AES.decrypt(v.toString().substring(MPW_PREFIX.length()), key);
                properties.put(k, value);
            } else {
                properties.put(k, v);
            }

        });
        return properties;
    }
}
