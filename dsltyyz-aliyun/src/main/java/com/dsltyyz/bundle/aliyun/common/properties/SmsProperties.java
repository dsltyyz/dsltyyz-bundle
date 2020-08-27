package com.dsltyyz.bundle.aliyun.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * 短信属性
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Data
public class SmsProperties {

    /**
     * 签名
     */
    private String signName;
}
