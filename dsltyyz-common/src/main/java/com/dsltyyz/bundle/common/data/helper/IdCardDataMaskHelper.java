package com.dsltyyz.bundle.common.data.helper;

import org.springframework.util.StringUtils;

/**
 * 身份数据处理
 *
 * @author dsltyyz
 */
public class IdCardDataMaskHelper implements DataHelper {

    public static int LENGTH = 18;

    /**
     * 数据处理
     *
     * @param o
     * @param param
     * @return
     */
    @Override
    public Object deal(Object o, String param) {
        if (o != null && !StringUtils.isEmpty(o.toString()) && o.toString().length() == LENGTH) {
            return o.toString().substring(0, 6) + "********" + o.toString().substring(14, 18);
        }
        return o;
    }
}
