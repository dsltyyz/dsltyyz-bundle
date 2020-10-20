package com.dsltyyz.bundle.office.excel.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Excel
 *
 * @author: dsltyyz
 * @date: 2019/04/10
 */
@Data
public class Excel {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * sheet列表
     */
    private List<ExcelSheet> excelSheetList = new ArrayList<>();

    /**
     * 是否开启debug模式
     */
    private Boolean debug = false;
}
