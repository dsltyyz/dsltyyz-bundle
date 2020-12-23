package com.dsltyyz.bundle.aliyun.config;

import com.dsltyyz.bundle.aliyun.client.pay.AliyunPayClient;
import com.dsltyyz.bundle.aliyun.client.sms.AliyunSmsClient;
import com.dsltyyz.bundle.aliyun.common.properties.PayProperties;
import com.dsltyyz.bundle.aliyun.common.properties.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * 条件注入PayProperties属性和Pay客户端
 *
 *
 * @author: dsltyyz
 * @date: 2020/08/27
 */
@Configuration
@EnableConfigurationProperties(PayProperties.class)
@ConditionalOnProperty(name = "spring.cloud.alicloud.pay.enable", havingValue = "true")
public class PayAutoConfiguration {

    @Bean
    public AliyunPayClient aliyunPayClient(){
        return new AliyunPayClient();
    }

}
