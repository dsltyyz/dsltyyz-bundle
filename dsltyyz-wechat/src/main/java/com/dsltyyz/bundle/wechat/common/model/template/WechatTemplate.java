package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * 微信模板消息
 *
 * @author: dsltyyz
 * @since: 2019-11-07
 */
@Data
public class WechatTemplate implements Serializable {

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 模板标题
     */
    private String title;

    /**
     * 模板所属行业的一级行业
     */
    private String primary_industry;

    /**
     * 模板所属行业的二级行业
     */
    private String deputy_industry;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 模板示例
     */
    private String example;

}
