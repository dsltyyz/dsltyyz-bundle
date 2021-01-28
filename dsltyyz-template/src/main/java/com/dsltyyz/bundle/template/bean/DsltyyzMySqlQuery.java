package com.dsltyyz.bundle.template.bean;

import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;

public class DsltyyzMySqlQuery extends MySqlQuery {

    @Override
    public String[] fieldCustom() {
        return new String[]{"Null","Default"};
    }

}
