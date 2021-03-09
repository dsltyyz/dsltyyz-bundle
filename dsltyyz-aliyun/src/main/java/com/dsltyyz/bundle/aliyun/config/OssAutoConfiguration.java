package com.dsltyyz.bundle.aliyun.config;

import com.dsltyyz.bundle.aliyun.client.oss.AliyunOssClient;
import com.dsltyyz.bundle.aliyun.common.properties.OssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * 条件注入OssProperties属性和Oss客户端
 *
 * spring-cloud-starter-alicloud-sms
 *   OssAutoConfiguration
 *     spring.cloud.alicloud.oss.enabled
 *
 * @author: dsltyyz
 * @since: 2020-08-27
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(name = "spring.cloud.alicloud.oss.enabled", havingValue = "true", matchIfMissing = true)
public class OssAutoConfiguration {

    @Bean
    public AliyunOssClient aliCloudOssClient(){
        return new AliyunOssClient();
    }

}
