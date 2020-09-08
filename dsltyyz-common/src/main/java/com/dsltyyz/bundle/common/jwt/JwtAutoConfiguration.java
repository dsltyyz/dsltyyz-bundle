package com.dsltyyz.bundle.common.jwt;

import com.dsltyyz.bundle.common.jwt.helper.JwtHelper;
import com.dsltyyz.bundle.common.rsa.helper.KeyPairHelper;
import com.dsltyyz.bundle.common.rsa.properties.KeyPairProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * JWT自动配置
 *
 * @author: dsltyyz
 * @date: 2020-9-8
 */
@Configuration
@EnableConfigurationProperties(KeyPairProperties.class)
public class JwtAutoConfiguration {

    @Bean
    public KeyPairHelper keyPairHelper(){
        return new KeyPairHelper();
    }

    @Bean
    @ConditionalOnBean(name = "keyPairHelper")
    public JwtHelper jwtHelper(){
        return new JwtHelper();
    }
}
