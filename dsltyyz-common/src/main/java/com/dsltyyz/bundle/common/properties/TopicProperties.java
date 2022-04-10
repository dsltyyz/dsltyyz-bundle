package com.dsltyyz.bundle.common.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * 主题属性
 *
 * @author: dsltyyz
 * @date: 2019-04-08
 */
@ApiModel(description = "主题属性")
@Data
public class TopicProperties {

    /**
     * 分组
     */
    @ApiModelProperty(value = "分组")
    private String group;

    /**
     * 订阅主题
     */
    @ApiModelProperty(value = "订阅主题")
    private String topic;

    /**
     * 标记
     */
    @ApiModelProperty(value = "标记")
    private String tag;
}
