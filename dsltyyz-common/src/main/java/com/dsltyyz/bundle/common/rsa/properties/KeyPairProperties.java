package com.dsltyyz.bundle.common.rsa.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * 加密解密配置
 *
 * @author: dsltyyz
 * @date: 2019/3/11
 */
@ConfigurationProperties(prefix = "key-pair")
@Data
public class KeyPairProperties {

    /**
     * 算法
     */
    private String algorithm = "RSA";

    /**
     * 私钥文件路径
     */
    private String privateKeyPath;

    /**
     * 公钥文件路径
     */
    private String publicKeyPath;

    /**
     * 长度
     */
    private Integer size = 1024;

    /**
     * 种子
     */
    private String seed;
}
