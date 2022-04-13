package com.dsltyyz.bundle.office.excel.handler;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 数字转整数数据类型处理
 *
 * @author dsltyyz
 * @date 2022-4-13
 */
public class NumericToIntegerDataHandler implements DataHandler {

    /**
     * 数据处理
     *
     * @param o
     * @return
     */
    @Override
    public String deal(String o, String param) {
        try {
            if (StringUtils.isEmpty(o)) {
                return null;
            }
            return new BigDecimal(o).setScale(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
