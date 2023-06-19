package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ${package.Service}.${table.serviceName};
import ${package.Entity?replace('entity','dto')}.*;
import ${package.Entity?replace('entity','vo')}.*;
import com.dsltyyz.bundle.common.page.*;
import com.dsltyyz.bundle.common.response.*;
import javax.validation.Valid;

/**
 * ${table.comment!} Controller
 *
 * @author ${author}
 * @date ${date}
 */
@Api(value = "${table.comment!} Controller", tags = {"${table.comment!}"})
@RestController
@RequestMapping("${package.ModuleName}/${entity?uncap_first}")
public class ${table.controllerName} {

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @ApiOperation(value = "新建${table.comment!}")
    @PostMapping(value = "")
    public CommonResponse create${entity}(@RequestBody ${entity}DTO dto){
        ${table.serviceName?uncap_first}.create${entity}(dto);
        return new CommonResponse();
    }
<#list table.fields as field>
    <#if field.keyFlag>

    @ApiOperation(value = "删除${table.comment!}")
    @DeleteMapping(value = "{${field.propertyName}}")
    public CommonResponse delete${entity}(@PathVariable("${field.propertyName}") ${field.propertyType} ${field.propertyName}){
        ${table.serviceName?uncap_first}.delete${entity}(${field.propertyName});
        return new CommonResponse();
    }

    @ApiOperation(value = "更新${table.comment!}")
    @PutMapping(value = "{${field.propertyName}}")
    public CommonResponse update${entity}(@PathVariable("${field.propertyName}") ${field.propertyType} ${field.propertyName}, @Valid @RequestBody ${entity}DTO dto){
        dto.set${field.propertyName?cap_first}(${field.propertyName});
        ${table.serviceName?uncap_first}.update${entity}(dto);
        return new CommonResponse();
    }

    @ApiOperation(value = "获取${table.comment!}详情")
    @GetMapping(value = "{${field.propertyName}}")
    public CommonResponse<${entity}VO> get${entity}(@PathVariable("${field.propertyName}") ${field.propertyType} ${field.propertyName}){
        return new CommonResponse<>(${table.serviceName?uncap_first}.get${entity}ById(${field.propertyName}));
    }
    </#if>
</#list>

    @ApiOperation(value = "获取${table.comment!}分页列表")
    @GetMapping(value = "")
    public CommonResponse<PageVO<${entity}VO>> get${entity}ByPage(@Valid ${entity}PageDTO pageDTO){
        return new CommonResponse<>(${table.serviceName?uncap_first}.get${entity}ListByPage(pageDTO));
    }

}