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
            String s = o.toString();
            return s.replace(s.substring(3, 7),"****");
        }
        return o;
    }

}
