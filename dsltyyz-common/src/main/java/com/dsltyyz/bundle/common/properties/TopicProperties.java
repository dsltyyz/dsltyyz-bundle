package com.dsltyyz.bundle.common.properties;

import lombok.Data;

/**
 * Description:
 * 主题属性
 *
 * @author: dsltyyz
 * @date: 2019/04/08
 */
@Data
public class TopicProperties {

    /**
     * 分组
     */
    private String group;

    /**
     * 订阅主题
     */
    private String topic;

    /**
     * 标记
     */
    private String tag;
}
