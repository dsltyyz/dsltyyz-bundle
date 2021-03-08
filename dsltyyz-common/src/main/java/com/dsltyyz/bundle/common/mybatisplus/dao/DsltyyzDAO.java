package com.dsltyyz.bundle.common.mybatisplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义DAO 用于sql注入
 * @author dsltyyz
 * @param <T>
 */
public interface DsltyyzDAO<T> extends BaseMapper<T> {

    /**
     * 如果要自动填充，@{@code Param}(xx) xx参数名必须是 list/collection/array 3个的其中之一
     *
     * @param List
     * @return
     */
    int insertBatchSomeColumn(@Param("list") List<T> List);

    /**
     * 逻辑删除时 更新实体中@TableField(fill = FieldFill.UPDATE)字段
     *
     * @param entity
     * @return
     */
    int deleteByIdWithFill(T entity);

    /**
     * 根据 ID 更新固定的那几个字段(但是不包含逻辑删除)
     *
     * @param entity
     * @return
     */
    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);
}
