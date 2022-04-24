package com.dsltyyz.bundle.wechat.config;

import com.dsltyyz.bundle.wechat.client.WechatClient;
import com.dsltyyz.bundle.wechat.common.property.WechatProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * 自动加载WechatProperties属性
 *
 * @author: dsltyyz
 * @date: 2019-11-22
 */
@Configuration
@EnableConfigurationProperties(WechatProperties.class)
@ConditionalOnProperty(name = "wechat.enabled", havingValue = "true", matchIfMissing = true)
public class WechatAutoConfiguration {

    @Bean
    public WechatClient wechatClient(){
        return new WechatClient();
    }
}
