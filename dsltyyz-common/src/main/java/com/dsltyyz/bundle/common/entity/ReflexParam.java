package com.dsltyyz.bundle.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 反射参数
 * </p>
 *
 * @author dsltyyz
 * @since 2021-01-21
 */
@ApiModel(description = "反射参数")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReflexParam implements Serializable {

    /**
     * 参数类
     */
    @ApiModelProperty(value = "参数类")
    private String paramClass;

    /**
     * 参数值
     */
    @ApiModelProperty(value = "参数值")
    private String paramValue;

    /**
     * 是否是集合
     */
    @ApiModelProperty(value = "是否是集合")
    private Boolean isCollection;

}
