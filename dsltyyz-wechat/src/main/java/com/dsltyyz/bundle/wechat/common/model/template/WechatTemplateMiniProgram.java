package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.*;

import java.io.Serializable;

/**
 * Description:
 * 微信数据值
 *
 * @author: dsltyyz
 * @since: 2019-11-07
 */
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class WechatTemplateMiniProgram implements Serializable {

    /**
     * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的服务号是绑定关联关系，暂不支持小游戏）
     */
    @NonNull
    private String appid;

    /**
     * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
     */
    private String pagepath;

}