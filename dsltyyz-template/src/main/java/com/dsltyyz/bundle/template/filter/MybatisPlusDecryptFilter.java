package com.dsltyyz.bundle.template.filter;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dsltyyz.bundle.template.util.MpwUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Mybatis Plus的解密拦截器
 *
 * @author dsltyyz
 * @date 2021-3-9
 */
@Slf4j
public class MybatisPlusDecryptFilter extends FilterAdapter {

    private static String MPW_PREFIX_KEY = "mpw.key";

    /**
     * 获取命令行中的key
     * dynamic-datasource-spring-boot-starter 无法注入ConfigurableEnvironment
     * 采用环境监听MybatisPlusEnvironmentListener静态赋值
     */
    public static ConfigurableEnvironment configurableEnvironment;

    public MybatisPlusDecryptFilter() {
        log.info("MybatisPlus加密解析Druid拦截器构造");
    }

    /**
     * 重写获取密码解密
     * 1. 参考DruidFilter的代码，取的是file类型的配置文件，这里从启动参数中获取即可
     * 2. 如果不配置，且不需要解密则放行
     *
     * @param dataSourceProxy
     */
    @Override
    public void init(DataSourceProxy dataSourceProxy) {
        if (!(dataSourceProxy instanceof DruidDataSource)) {
            log.error("ConfigLoader only support DruidDataSource");
        }
        DruidDataSource dataSource = (DruidDataSource) dataSourceProxy;
        String mpwKey = null;
        for (PropertySource<?> ps : configurableEnvironment.getPropertySources()) {
            if (ps instanceof SimpleCommandLinePropertySource) {
                SimpleCommandLinePropertySource source = (SimpleCommandLinePropertySource) ps;
                mpwKey = source.getProperty(MPW_PREFIX_KEY);
                break;
            }
        }
        if (StringUtils.isNotBlank(mpwKey)) {
            // 含有加密key 将所有参数过滤一次
            Properties properties = decryptProperties(dataSource, mpwKey);
            try {
                // 将信息配置进druid
                DruidDataSourceFactory.config(dataSource, properties);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过命令行的密钥进行解密
     *
     * @param dataSource 数据源
     * @param mpwKey     解密key
     * @return
     */
    private Properties decryptProperties(DruidDataSource dataSource, String mpwKey) {
        Properties mpwProperties = new Properties();
        // 先解密
        try {
            mpwProperties.setProperty(DruidDataSourceFactory.PROP_URL, dataSource.getUrl());
            mpwProperties.setProperty(DruidDataSourceFactory.PROP_USERNAME, dataSource.getUsername());
            mpwProperties.setProperty(DruidDataSourceFactory.PROP_PASSWORD, dataSource.getPassword());
        } catch (Exception e) {
            log.info("druid decrypt failed!");
            e.printStackTrace();
        }
        return MpwUtils.decrypt(mpwProperties, mpwKey);
    }

}
