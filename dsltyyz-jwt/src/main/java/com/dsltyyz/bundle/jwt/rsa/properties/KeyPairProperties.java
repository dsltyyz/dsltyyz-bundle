package com.dsltyyz.bundle.jwt.rsa.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * 加密解密配置
 *
 * @author: dsltyyz
 * @date: 2019-3-11
 */
@ApiModel(description = "加密解密配置")
@ConfigurationProperties(prefix = "key-pair")
@Data
public class KeyPairProperties {

    /**
     * 算法 默认RSA
     */
    @ApiModelProperty(value = "算法 默认RSA")
    private String algorithm = "RSA";

    /**
     * 私钥文件路径
     */
    @ApiModelProperty(value = "私钥文件路径")
    private String privateKeyPath;

    /**
     * 公钥文件路径
     */
    @ApiModelProperty(value = "公钥文件路径")
    private String publicKeyPath;

    /**
     * 长度 默认1024
     */
    @ApiModelProperty(value = "长度 默认1024")
    private Integer size = 1024;

    /**
     * 种子
     */
    @ApiModelProperty(value = "种子")
    private String seed;
}
