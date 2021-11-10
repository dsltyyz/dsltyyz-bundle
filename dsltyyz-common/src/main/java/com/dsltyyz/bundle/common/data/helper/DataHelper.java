package com.dsltyyz.bundle.common.data.helper;

/**
 * 数据处理
 *
 * @author dsltyyz
 */
public interface DataHelper {

    /**
     * 数据处理
     *
     * @param o
     * @return
     */
    default Object deal(Object o, String param) {
        return null;
    }
}
