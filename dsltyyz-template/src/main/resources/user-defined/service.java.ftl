package ${package.Service};

import ${package.Entity}.*;
import ${package.Entity?replace('entity','dto')}.*;
import ${package.Entity?replace('entity','vo')}.*;
import com.dsltyyz.bundle.template.page.*;
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} Service
 * </p>
 *
 * @author ${author}
 * @date ${date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * 新建${table.comment!}
     *
     * @param dto
     */
    void create${entity}(${entity}DTO dto);

    /**
     * 更新${table.comment!}
     *
     * @param dto
     */
    void update${entity}(${entity}DTO dto);

<#list table.fields as field>
    <#if field.keyFlag>
    /**
     * 删除${table.comment!}
     *
     * @param ${field.propertyName}
     */
    void delete${entity}(${field.propertyType} ${field.propertyName});

    /**
     * 查询${table.comment!}
     *
     * @param ${field.propertyName}
     * @return
     */
    ${entity}VO get${entity}ById(${field.propertyType} ${field.propertyName});

    </#if>
</#list>
    /**
     * 分页查询${table.comment!}
     *
     * @param pageDTO
     * @return
     */
    PageVO<${entity}VO> get${entity}ListByPage(${entity}PageDTO pageDTO);
}