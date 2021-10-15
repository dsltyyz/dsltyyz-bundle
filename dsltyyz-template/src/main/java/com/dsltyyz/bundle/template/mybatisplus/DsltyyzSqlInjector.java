package com.dsltyyz.bundle.template.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;

import java.util.List;

/**
 * 自定义SQL注入器
 *
 * @author dsltyyz
 */
public class DsltyyzSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        //批量插入
        methodList.add(new InsertBatchSomeColumn());
        //批量更新
        methodList.add(new UpdateBatchMethod());
        //删除填充
        methodList.add(new LogicDeleteByIdWithFill());
        //更新
        methodList.add(new AlwaysUpdateSomeColumnById());
        return methodList;
    }
}
