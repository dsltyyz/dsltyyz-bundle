package com.dsltyyz.bundle.wechat.common.constant;

/**
 * Description:
 * 微信授权传输模式
 *
 * @author: dsltyyz
 * @date: 2019-03-21
 */
public interface WechatOauthTransferMode {

    /**
     * 明文模式
     */
    int CLEAR_TEXT_MODE = 0;

    /**
     * 兼容模式
     */
    int COMPATIBILITY_MODE = 1;

    /**
     * 安全模式
     */
    int SECURITY_MODE = 2;

}