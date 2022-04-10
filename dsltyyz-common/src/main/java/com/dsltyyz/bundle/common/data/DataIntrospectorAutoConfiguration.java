package com.dsltyyz.bundle.common.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

/**
 * Description:
 * 自动加载缓存客户端
 *
 * @author: dsltyyz
 * @date: 2019-11-19
 */
@Configuration
public class DataIntrospectorAutoConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        //此处配置会导致spring.jackson.time-zone设置失效
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);

        ObjectMapper objectMapper = converter.getObjectMapper();
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.setAnnotationIntrospector(new DataFormatAnnotationIntrospector());
        return converter;
    }

}
