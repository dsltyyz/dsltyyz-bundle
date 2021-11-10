package com.dsltyyz.bundle.template.util;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.dsltyyz.bundle.common.util.EncryptUtils;

/**
 * Description:
 * Mybatis Plus 数据加密及解密AES工具
 *
 * @author: dsltyyz
 * @date: 2021-03-09
 */
public class AesUtils {

    public static final String KEY = EncryptUtils.MD5("AES","dsltyyz",16);

    /**
     * 加密
     * @param content
     * @return
     */
    public static String encrypt(String content){
        return AES.encrypt(content, KEY);
    }

    /**
     * 解密
     * @param content
     * @return
     */
    public static String decrypt(String content){
        return AES.decrypt(content, KEY);
    }
}
