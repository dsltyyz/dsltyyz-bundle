package com.dsltyyz.bundle.office.excel.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Excel Sheet
 *
 * @author: dsltyyz
 * @date: 2019/04/10
 */
@ApiModel(description = "Excel工作表")
@Data
public class ExcelSheet {

    /**
     * sheet名称
     */
    @ApiModelProperty(value = "sheet名称", required = true)
    private String sheetName;

    /**
     * 头显示数据
     */
    @ApiModelProperty(value = "头显示数据")
    private List<String> headList;

    /**
     * 配置显示
     */
    @ApiModelProperty(value = "配置显示", required = true)
    private List<ExcelSheetColumnProperty> propertyList;

    /**
     * 显示数据
     */
    @ApiModelProperty(value = "显示数据", required = true)
    private List list;
}
