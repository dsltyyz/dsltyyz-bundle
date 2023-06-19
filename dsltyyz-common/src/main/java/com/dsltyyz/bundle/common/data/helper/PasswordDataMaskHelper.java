package com.dsltyyz.bundle.common.data.helper;

import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * 密码数据处理
 *
 * @author dsltyyz
 */
public class PasswordDataMaskHelper implements DataHelper {

    /**
     * 密码数据处理
     *
     * @param o
     * @param param
     * @return
     */
    @Override
    public Object deal(Object o, String param) {
        if (o != null && !StringUtils.isEmpty(o.toString())) {
            return String.join("", Collections.nCopies(o.toString().length(), "*"));
        }
        return o;
    }

}
