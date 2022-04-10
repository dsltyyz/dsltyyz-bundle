package com.dsltyyz.bundle.aliyun.config;

import com.dsltyyz.bundle.aliyun.client.sms.AliyunSmsClient;
import com.dsltyyz.bundle.aliyun.common.properties.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * 条件注入SmsProperties属性和Sms客户端
 *
 * spring-cloud-starter-alicloud-sms
 *   SmsAutoConfiguration
 *     spring.cloud.alicloud.sms.enable
 *
 * @author: dsltyyz
 * @date: 2020-08-27
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(name = "spring.cloud.alicloud.sms.enable", havingValue = "true", matchIfMissing = true)
public class SmsAutoConfiguration {

    @Bean
    public AliyunSmsClient aliCloudSmsClient(){
        return new AliyunSmsClient();
    }

}
