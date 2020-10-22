package com.dsltyyz.bundle.office.excel.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Excel")
@Data
public class Excel {

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * sheet列表
     */
    @ApiModelProperty(value = "sheet列表", required = true)
    private List<ExcelSheet> excelSheetList = new ArrayList<>();

    /**
     * 是否开启debug模式
     */
    @ApiModelProperty(value = "是否开启debug模式 默认false")
    private Boolean debug = false;
}
