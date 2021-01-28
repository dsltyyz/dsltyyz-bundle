package com.dsltyyz.bundle.template.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表字段
 * @author dsltyyz
 */
@ApiModel(description = "表字段")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsltyyzTableField {

    /**
     * 是否是主键
     */
    @ApiModelProperty(value = "是否是主键")
    private Boolean keyFlag;

    /**
     * 是否是自增
     */
    @ApiModelProperty(value = "是否是自增")
    private Boolean keyIdentityFlag;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;

    /**
     * 是否为空
     */
    @ApiModelProperty(value = "是否为空")
    private Boolean nullFlag;

    /**
     * 默认值
     */
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
}
