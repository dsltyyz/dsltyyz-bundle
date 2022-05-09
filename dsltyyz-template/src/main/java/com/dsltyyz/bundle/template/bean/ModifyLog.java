package com.dsltyyz.bundle.template.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修订日志
 * @author dsltyyz
 */
@ApiModel(description = "修订日志")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyLog {

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "修订内容")
    private String modifyContent;

    @ApiModelProperty(value = "修订人")
    private String author;

    @ApiModelProperty(value = "审核人")
    private String auditor;

    @ApiModelProperty(value = "修订日期")
    private String modifyTime;

}
