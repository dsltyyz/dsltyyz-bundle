package com.dsltyyz.bundle.common.filter;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Mybatis Plus的解密拦截器
 *
 * @author dsltyyz
 * @since 2021-3-9
 */
@Component
@Slf4j
public class MybatisPlusDecryptFilter extends FilterAdapter {

    private static String MPW_PREFIX = "mpw:";

    /**
     * 获取命令行中的key
     */
    @Resource
    private ConfigurableEnvironment configurableEnvironment;

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
                mpwKey = source.getProperty("mpw.key");
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
        Properties properties = new Properties();
        // 先解密
        try {
            if (dataSource.getUrl().startsWith(MPW_PREFIX)) {
                properties.setProperty(DruidDataSourceFactory.PROP_URL, AES.decrypt(dataSource.getUrl().substring(4), mpwKey));
            }
            if (dataSource.getUsername().startsWith(MPW_PREFIX)) {
                properties.setProperty(DruidDataSourceFactory.PROP_USERNAME, AES.decrypt(dataSource.getUsername().substring(4), mpwKey));
            }
            if (dataSource.getPassword().startsWith(MPW_PREFIX)) {
                properties.setProperty(DruidDataSourceFactory.PROP_PASSWORD, AES.decrypt(dataSource.getPassword().substring(4), mpwKey));
            }
        } catch (Exception e) {
            log.info("druid decrypt failed!");
            e.printStackTrace();
        }
        return properties;
    }


}
