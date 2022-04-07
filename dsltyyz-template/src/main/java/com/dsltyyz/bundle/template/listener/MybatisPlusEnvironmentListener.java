package com.dsltyyz.bundle.template.listener;

import com.dsltyyz.bundle.template.filter.MybatisPlusDecryptFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author dsltyyz
 * @date 2022-4-2
 */
@Order
@Slf4j
public class MybatisPlusEnvironmentListener implements SpringApplicationRunListener {

    public MybatisPlusEnvironmentListener(SpringApplication application, String[]  args){
        log.info("服务环境监听");
    }

    @Override
    public void starting() {
    }


    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }


    @Override
    public void started(ConfigurableApplicationContext context) {
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        MybatisPlusDecryptFilter.configurableEnvironment = environment;
    }
}
