package com.dsltyyz.bundle.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * 编号和名称VO
 *
 * @author: dsltyyz
 * @date: 2020-09-02
 */
@ApiModel(description = "编号和名称VO")
@Data
public class IdAndNameVO implements Serializable {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;
}
