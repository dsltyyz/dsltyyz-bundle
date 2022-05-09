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

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "是否是主键")
    private Boolean keyFlag;

    @ApiModelProperty(value = "是否是自增")
    private Boolean keyIdentityFlag;

    @ApiModelProperty(value = "是否为空")
    private Boolean nullFlag;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "备注")
    private String comment;

}
