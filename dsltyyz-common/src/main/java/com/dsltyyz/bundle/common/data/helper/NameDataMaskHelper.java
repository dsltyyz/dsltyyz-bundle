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
            return o.toString().substring(0, 1) + "*" + o.toString().substring(2);
        }
        return o;
    }

}
