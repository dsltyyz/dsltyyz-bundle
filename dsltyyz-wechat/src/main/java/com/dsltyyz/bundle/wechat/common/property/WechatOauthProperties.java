package com.dsltyyz.bundle.wechat.common.property;

import com.dsltyyz.bundle.wechat.common.constant.WechatOauthDataType;
import com.dsltyyz.bundle.wechat.common.constant.WechatOauthTransferMode;
import lombok.Data;

/**
 * Description:
 * 微信账号属性
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@Data
public class WechatOauthProperties extends WechatSecurityProperties{

    /**
     * 公众号Token
     */
    private String token;

    /**
     * 消息加密方式 0:明文模式(默认), 1:兼容模式, 2:安全模式(推荐)
     */
    private Integer encodingType = WechatOauthTransferMode.CLEAR_TEXT_MODE;

    /**
     * 消息加密密钥(43位字符组成A-Za-z0-9)
     */
    private String encodingAesKey;

    /**
     * 数据格式 XML JSON
     */
    private String dataType= WechatOauthDataType.JSON;
}
