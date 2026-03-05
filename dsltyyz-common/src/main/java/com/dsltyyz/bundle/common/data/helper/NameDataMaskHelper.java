package com.dsltyyz.bundle.common.data.helper;

import org.springframework.util.StringUtils;

/**
 * 姓名数据处理
 *
 * @author dsltyyz
 */
public class NameDataMaskHelper implements DataHelper {

    /**
     * 姓名数据处理
     *
     * @param o
     * @param param
     * @return
     */
    @Override
    public Object deal(Object o, String param) {
        if (o != null && !StringUtils.isEmpty(o.toString())) {
            String s = o.toString();
            return s.replace(s.substring(1, 2),"*");
        }
        return o;
    }

}
