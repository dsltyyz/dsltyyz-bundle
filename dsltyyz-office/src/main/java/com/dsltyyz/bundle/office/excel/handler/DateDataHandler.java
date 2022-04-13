package com.dsltyyz.bundle.office.excel.handler;

import com.dsltyyz.bundle.common.util.DateUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;

/**
 * 日期数据类型处理
 *
 * @author dsltyyz
 * @date 2022-4-13
 */
public class DateDataHandler implements DataHandler{

    /**
     * 数据处理
     *
     * @param o
     * @return
     */
    @Override
    public String deal(String o, String param) {
        try {
            if(StringUtils.isEmpty(o)){
                return null;
            }
            return DateUtils.format(DateUtils.parse(o, param));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
