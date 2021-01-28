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
public class ModityLog {

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

    /**
     * 修订内容
     */
    @ApiModelProperty(value = "修订内容")
    private String modifyContent;

    /**
     * 修订人
     */
    @ApiModelProperty(value = "修订人")
    private String author;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String auditor;

    /**
     * 修订日期
     */
    @ApiModelProperty(value = "修订日期")
    private String modifyTime;

}
