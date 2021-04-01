package com.dsltyyz.bundle.template.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.dsltyyz.bundle.template.mybatisplus.DsltyyzMetaObjectHandler;
import com.dsltyyz.bundle.template.mybatisplus.DsltyyzSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis plus 分页配置
 *
 * @author dsltyyz
 * @since 2020-8-28
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁（仅适合高读取，少更新）
     *
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
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
