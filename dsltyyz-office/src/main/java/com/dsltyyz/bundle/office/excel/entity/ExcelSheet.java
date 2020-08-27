package com.dsltyyz.bundle.office.excel.entity;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Excel Sheet
 *
 * @author: dsltyyz
 * @date: 2019/04/10
 */
@Data
public class ExcelSheet {

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 配置显示
     */
    private List<ExcelSheetColumnProperty> propertyList;

    /**
     * 显示数据
     */
    private List list;
}
