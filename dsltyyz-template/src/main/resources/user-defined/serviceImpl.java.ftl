package ${package.ServiceImpl};

import ${package.Entity}.*;
import ${package.Entity?replace('entity','dto')}.*;
import ${package.Entity?replace('entity','vo')}.*;
import com.dsltyyz.bundle.template.page.*;
import com.dsltyyz.bundle.template.util.PageUtil;
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;

/**
 * <p>
 * ${table.comment!} Service Impl
 * </p>
 *
 * @author ${author}
 * @date ${date}
 */
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Resource
    private ${table.mapperName} ${table.mapperName?uncap_first};

    /**
     * 新建
     *
     * @param dto
     */
    @Override
    public void create${entity}(${entity}DTO dto){
        ${entity} ${entity?uncap_first} = new ${entity}();
        BeanUtils.copyProperties(dto, ${entity?uncap_first});
        ${table.mapperName?uncap_first}.insert(${entity?uncap_first});
    }

<#list table.fields as field>
    <#if field.keyFlag>
    /**
     * 更新
     *
     * @param dto
     */
    @Override
    public void update${entity}(${entity}DTO dto){
        ${entity} ${entity?uncap_first} =  ${table.mapperName?uncap_first}.selectById(dto.get${field.propertyName?cap_first}());
        Assert.notNull(${entity?uncap_first}, "该ID对应${table.comment!}不存在");
        BeanUtils.copyProperties(dto, ${entity?uncap_first});
        ${table.mapperName?uncap_first}.updateById(${entity?uncap_first});
    }

    /**
     * 删除
     *
     * @param ${field.propertyName}
     */
    @Override
    public void delete${entity}(${field.propertyType} ${field.propertyName}){
        ${entity} ${entity?uncap_first} =  ${table.mapperName?uncap_first}.selectById(${field.propertyName});
        Assert.notNull(${entity?uncap_first}, "该ID对应${table.comment!}不存在");
        ${table.mapperName?uncap_first}.deleteById(${field.propertyName});
    }

    /**
     * 查询
     *
     * @param ${field.propertyName}
     * @return
     */
    @Override
    public ${entity}VO get${entity}ById(${field.propertyType} ${field.propertyName}){
        ${entity}VO  vo = new ${entity}VO();
        ${entity} entity =  ${table.mapperName?uncap_first}.selectById(${field.propertyName});
        Assert.notNull(entity, "该ID对应${table.comment!}不存在");
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    </#if>
</#list>
    /**
     * 分页查询
     *
     * @param pageDTO
     * @return
     */
    @Override
    public PageVO<${entity}VO> get${entity}ListByPage(${entity}PageDTO pageDTO){
        PageVO<${entity}VO> vo = new PageVO<>();

        IPage<${entity}> iPage = new Page<>();
        //排序字段转换
        PageUtil.convertOrderItem(pageDTO, ${entity}.class);
        BeanUtils.copyProperties(pageDTO, iPage);

        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        //条件筛选
        iPage = ${table.mapperName?uncap_first}.selectPage(iPage, queryWrapper);
        BeanUtils.copyProperties(iPage, vo);
        vo.setRecords(new ArrayList<>());
        iPage.getRecords().forEach(${entity?uncap_first} -> {
            ${entity}VO ${entity?uncap_first}VO = new ${entity}VO();
            BeanUtils.copyProperties(${entity?uncap_first}, ${entity?uncap_first}VO);
            vo.getRecords().add(${entity?uncap_first}VO);
        });
        return vo;
    }

}