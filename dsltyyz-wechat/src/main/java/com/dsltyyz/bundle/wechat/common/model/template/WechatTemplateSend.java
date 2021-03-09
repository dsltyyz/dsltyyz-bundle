package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.*;

/**
 * Description:
 * 发送模板消息
 *
 * @author: dsltyyz
 * @since: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class WechatTemplateSend {

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
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private WechatTemplateMiniProgram miniprogram;

    /**
     * 模板数据
     */
    @NonNull
    private String data;

}
