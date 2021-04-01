package com.dsltyyz.bundle.wechat.common.model.pay;

import com.dsltyyz.bundle.common.util.HttpUtils;
import com.github.wxpay.sdk.WXPayConfig;
import lombok.*;

import java.io.InputStream;

/**
 * Description:
 * 微信数据
 *
 * @author: dsltyyz
 * @since: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPayConfig implements WXPayConfig {

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
        this.certStream = certStream;
    }

    public WechatPayConfig(@NonNull String appID, @NonNull String mchID, @NonNull String key, String certUrl) {
        this.appID = appID;
        this.mchID = mchID;
        this.key = key;
        this.certStream = HttpUtils.doGetInputStream(certUrl, null, null);
    }
}
