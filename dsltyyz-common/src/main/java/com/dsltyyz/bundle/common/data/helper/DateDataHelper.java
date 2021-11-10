package com.dsltyyz.bundle.common.data.helper;

import com.dsltyyz.bundle.common.util.DateUtils;
import com.dsltyyz.bundle.common.util.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 日期数据处理
 *
 * @author dsltyyz
 */
public class DateDataHelper implements DataHelper {

    /**
     * 日期数据处理
     *
     * @param o
     * @param param
     * @return
     */
    @Override
    public Object deal(Object o, String param) {
        if (o != null) {
            if(o instanceof Date){
                return DateUtils.format((Date)o, param);
            }else if(o instanceof LocalDateTime){
                return LocalDateTimeUtils.format((LocalDateTime)o, param);
            }
        }
        return o;
    }

}
