package com.dsltyyz.bundle.aliyun.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * OSS属性
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@ConfigurationProperties("spring.cloud.alicloud.oss")
@Data
public class OssProperties {

    /**
     * 包名
     */
    private String bucketName;

    /**
     * 节点
     */
    private String endpoint;
}
