package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信小程序订阅消息
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@Data
public class WechatMiniTemplate implements Serializable {

    /**
     * 添加至帐号下的模板 id，发送小程序订阅消息时所需
     */
    private String priTmplId;

    /**
     *	模版标题
     */
    private String title;

    /**
     *模版内容
     */
    private String content;

    /**
     *模板内容示例
     */
    private String example;

    /**
     *枚举参数值范围
     */
    private List<WechatMiniKeywordEnumValue> keywordEnumValueList;

    /**
     *模版类型，2 为一次性订阅，3 为长期订阅
     */
    private int type;

}
