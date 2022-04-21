package com.dsltyyz.bundle.wechat.common.model.pay;

import com.dsltyyz.bundle.common.util.HttpUtils;
import com.github.wxpay.sdk.WXPayConfig;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Description:
 * 微信支付配置
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPayConfig implements WXPayConfig, Serializable {

    /**
     * 公众账号ID
     */
    @NonNull
    private String appID;
    /**
     * 商户号
     */
    @NonNull
    private String mchID;
    /**
     * 子商户号
     */
    private String subMchID;
    /**
     * API 密钥
     */
    @NonNull
    private String key;
    /**
     * 商户证书内容
     */
    private InputStream certStream;
    /**
     * HTTP(S) 连接超时时间，单位毫秒
     */
    private int httpConnectTimeoutMs = 2000;
    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     */
    private int httpReadTimeoutMs = 10000;

    public WechatPayConfig(@NonNull String appID, @NonNull String mchID, @NonNull String key, InputStream certStream) {
        this.appID = appID;
        this.mchID = mchID;
        this.key = key;
        if (certStream != null) {
            this.certStream = certStream;
        }
    }

    public WechatPayConfig(@NonNull String appID, @NonNull String mchID, @NonNull String key, String certUrl) {
        this.appID = appID;
        this.mchID = mchID;
        this.key = key;
        if (!StringUtils.isEmpty(certUrl)) {
            this.certStream = HttpUtils.doGetInputStream(certUrl, null, null);
        }
    }
}
