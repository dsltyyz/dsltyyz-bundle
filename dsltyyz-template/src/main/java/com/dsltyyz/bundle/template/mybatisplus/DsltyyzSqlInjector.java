package com.dsltyyz.bundle.template.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * 自定义SQL注入器
 *
 * @author dsltyyz
 */
public class DsltyyzSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        //批量插入
        methodList.add(new InsertBatchSomeColumn());
        //批量更新
        methodList.add(new UpdateBatchMethod());
        //更新
        methodList.add(new AlwaysUpdateSomeColumnById());
        return methodList;
    }
}
