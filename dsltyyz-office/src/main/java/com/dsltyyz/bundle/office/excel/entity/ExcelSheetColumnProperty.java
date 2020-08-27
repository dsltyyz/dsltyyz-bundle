package com.dsltyyz.bundle.office.excel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Excel Sheet 列对应属性
 *
 * @author: dsltyyz
 * @date: 2019/04/10
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExcelSheetColumnProperty {

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列属性
     */
    private String columnProperty;
}
