package com.dsltyyz.bundle.common.util;

import java.util.UUID;

/**
 * Description:
 * UUID 工具类
 *
 * @author: dsltyyz
 * @date: 2019-3-21
 */
public class UUIDUtils {

    /**
     * 得到32位UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 得到length位UUID
     * @return
     */
    public static String getUUIDByLength(int length){
        return getUUID().substring(0,length-1);
    }
}
