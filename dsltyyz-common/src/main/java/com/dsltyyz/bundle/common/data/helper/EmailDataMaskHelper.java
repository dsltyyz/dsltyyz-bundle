package com.dsltyyz.bundle.common.data.helper;

import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * 邮箱数据处理
 *
 * @author dsltyyz
 */
public class EmailDataMaskHelper implements DataHelper {


    /**
     * 数据处理
     *
     * @param o
     * @param param
     * @return
     */
    @Override
    public Object deal(Object o, String param) {
        if (o != null && !StringUtils.isEmpty(o.toString())) {
            String s = o.toString();
            String s1 = s.substring(0, s.indexOf("@"));
            return s.replace(s1, String.join("", Collections.nCopies(s1.length(), "*")));
        }
        return o;
    }
}
