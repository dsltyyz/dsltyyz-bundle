package com.dsltyyz.bundle.aliyun.common.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * 支付属性
 *
 * @author: dsltyyz
 * @date: 2019-11-19
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "支付属性")
@ConfigurationProperties(prefix = "spring.cloud.alicloud.pay")
@Data
public class PayProperties {

    /**
     * 公钥模式
     */
    public static String KEY_MODE="KEY";

    /**
     * 证书模式
     */
    public static String CERT_MODE="CERT";

    /**
     * 服务器URL
     */
    @ApiModelProperty(value = "服务器URL")
    private String serverUrl = "https://openapi.alipay.com/gateway.do";

    /**
     * 应用ID
     */
    @NonNull
    @ApiModelProperty(value = "应用ID")
    private String appId;

    /**
     * 应用私钥
     */
    @NonNull
    @ApiModelProperty(value = "应用私钥")
    private String privateKey;

    /**
     * 字符集编码
     */
    @ApiModelProperty(value = "字符集编码")
    private String charset = "utf-8";

    /**
     * 字符集编码
     */
    @ApiModelProperty(value = "字符集编码")
    private String format = "json";

    /**
     * 签名类型
     */
    @ApiModelProperty(value = "签名类型")
    private String signType  = "RSA2";

    /**
     * 模式 公钥 证书
     */
    @ApiModelProperty(value = "模式 公钥 证书  默认公钥")
    private String mode = KEY_MODE;

    /**
     * 模式为1公钥时 支付宝公钥必填
     */
    @ApiModelProperty(value = "模式为公钥时 支付宝公钥必填")
    private String alipayPublicKey;

    /**
     * 模式为2证书时 应用公钥证书文件路径
     */
    @ApiModelProperty(value = "模式为证书时 应用公钥证书文件路径")
    private String certPath;

    /**
     * 模式为2证书时 支付宝公钥证书文件路径
     */
    @ApiModelProperty(value = "模式为证书时 支付宝公钥证书文件路径")
    private String alipayPublicCertPath;

    /**
     * 模式为2证书时 支付宝根证书文件路径
     */
    @NonNull
    @ApiModelProperty(value = "模式为证书时 支付宝根证书文件路径")
    private String rootCertPath;

}
