package com.dsltyyz.bundle.office.excel.handler;

/**
 * 数据类型处理
 *
 * @author dsltyyz
 * @date 2022-4-13
 */
public interface DataHandler {

    /**
     * 数据处理
     *
     * @param o
     * @return
     */
    default String deal(String o, String param) {
        return null;
    }
}
