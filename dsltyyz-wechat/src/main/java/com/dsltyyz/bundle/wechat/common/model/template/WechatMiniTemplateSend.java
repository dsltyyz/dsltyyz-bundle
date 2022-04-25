package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.*;

import java.io.Serializable;

/**
 * Description:
 * 发送小程序订阅消息
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class WechatMiniTemplateSend implements Serializable {

    /**
     * 接收者openid
     */
    @NonNull
    private String touser;

    /**
     *	模板ID
     */
    @NonNull
    private String template_id;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     */
    private String page;

    /**
     * 模板数据
     */
    @NonNull
    private Object data;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    private String miniprogram_state;

    /**
     * 进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
     */
    private String lang;
}
