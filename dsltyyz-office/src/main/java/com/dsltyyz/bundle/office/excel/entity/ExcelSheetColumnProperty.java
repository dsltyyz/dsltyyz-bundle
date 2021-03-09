package com.dsltyyz.bundle.office.excel.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Excel Sheet 列对应属性
 *
 * @author: dsltyyz
 * @since: 2019-04-10
 */
@ApiModel(description = "Excel工作表列对应属性")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExcelSheetColumnProperty {

    /**
     * 列名称
     */
    @ApiModelProperty(value = "列名称", required = true)
    private String columnName;

    /**
     * 列属性
     */
    @ApiModelProperty(value = "列属性", required = true)
    private String columnProperty;
}
