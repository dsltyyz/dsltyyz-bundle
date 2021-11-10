package com.dsltyyz.bundle.common.data.helper;

import org.springframework.util.StringUtils;

/**
 * 手机号数据处理
 *
 * @author dsltyyz
 */
public class MobileDataMaskHelper implements DataHelper {

    public static int LENGTH = 11;

    /**
     * 数据处理
     *
     * @param o
     * @param param
     * @return
     */
    @Override
    public Object deal(Object o, String param) {
        if (!StringUtils.isEmpty(o.toString()) && o.toString().length() == LENGTH) {
            return o.toString().substring(0, 3) + "****" + o.toString().substring(7, 11);
        }
        return o;
    }

}
