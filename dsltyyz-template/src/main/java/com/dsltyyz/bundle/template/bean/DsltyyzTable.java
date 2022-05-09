package com.dsltyyz.bundle.template.bean;

import com.dsltyyz.bundle.office.excel.annotation.ExcelColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 表
 * @author dsltyyz
 */
@ApiModel(description = "表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsltyyzTable {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ExcelColumn(false)
    @ApiModelProperty(value = "字段")
    private List<DsltyyzTableField> fields = new ArrayList<>();
}
