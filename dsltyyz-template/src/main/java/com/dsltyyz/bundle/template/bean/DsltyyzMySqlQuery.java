package com.dsltyyz.bundle.template.bean;

import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;

/**
 * 自定义Mysql查询字段
 * @author dsltyyz
 */
public class DsltyyzMySqlQuery extends MySqlQuery {

    @Override
    public String[] fieldCustom() {
        return new String[]{"Null","Default"};
    }

}
