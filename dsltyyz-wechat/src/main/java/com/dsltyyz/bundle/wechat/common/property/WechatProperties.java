package com.dsltyyz.bundle.wechat.common.property;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * 微信属性
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@ApiModel(description = "OSS属性")
@ConfigurationProperties("wechat")
@Data
public class WechatProperties {

    /**
     * 是否调试
     */
    @ApiModelProperty(value = "是否调试")
    private Boolean debug = false;

    /**
     * 授权属性
     */
    @ApiModelProperty(value = "授权属性")
    private WechatOauthProperties oauth;

    /**
     * 支付属性
     */
    @ApiModelProperty(value = "支付属性")
    private WechatPayProperties pay;
}
