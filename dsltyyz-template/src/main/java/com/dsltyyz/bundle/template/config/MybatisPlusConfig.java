package com.dsltyyz.bundle.template.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.dsltyyz.bundle.template.mybatisplus.DsltyyzMetaObjectHandler;
import com.dsltyyz.bundle.template.mybatisplus.DsltyyzSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis plus 分页配置
 *
 * @author dsltyyz
 * @date 2020-8-28
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * 配置拦截器
     */
    @Bean
    public MybatisPlusInterceptor MybatisPlusInterceptor () {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //乐观锁（仅适合高读取，少更新）
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * SQL注入器（暂只支持Mysql）
     *
     * @return
     */
    @Bean
    public DsltyyzSqlInjector dsltyyzSqlInjector() {
        return new DsltyyzSqlInjector();
    }

    /**
     * 自动填充
     *
     * @return
     */
    @Bean
    public DsltyyzMetaObjectHandler dsltyyzMetaObjectHandler() {
        return new DsltyyzMetaObjectHandler();
    }

}
