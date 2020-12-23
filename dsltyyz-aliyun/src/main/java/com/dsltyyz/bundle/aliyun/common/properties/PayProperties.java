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
 * @date: 2019/11/19
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "支付属性")
@ConfigurationProperties(prefix = "spring.cloud.alicloud.pay")
@Data
public class PayProperties {

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
     * 应用公钥证书文件路径
     */
    @NonNull
    @ApiModelProperty(value = "应用公钥证书文件路径")
    private String certPath;

    /**
     * 支付宝公钥证书文件路径
     */
    @NonNull
    @ApiModelProperty(value = "支付宝公钥证书文件路径")
    private String alipayPublicCertPath;

    /**
     * 支付宝根证书文件路径
     */
    @NonNull
    @ApiModelProperty(value = "支付宝根证书文件路径")
    private String rootCertPath;

}
