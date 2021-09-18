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
import com.dsltyyz.bundle.template.page.*;
import com.dsltyyz.bundle.common.response.*;
import javax.validation.Valid;

/**
 * ${table.comment!} Controller
 *
 * @author ${author}
 * @since ${date}
 */
@Api(value = "${table.comment!}控制器", tags = {"${table.comment!}"})
@RestController
@RequestMapping("${package.ModuleName}/${entity?uncap_first}")
public class ${table.controllerName} {

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @ApiOperation(value = "增")
    @PostMapping(value = "")
    public CommonResponse create(@RequestBody ${entity}DTO dto){
        ${table.serviceName?uncap_first}.create${entity}(dto);
        return new CommonResponse();
    }
<#list table.fields as field>
    <#if field.keyFlag>

    @ApiOperation(value = "删")
    @DeleteMapping(value = "{${field.propertyName}}")
    public CommonResponse delete(@PathVariable("${field.propertyName}") ${field.propertyType} ${field.propertyName}){
        ${table.serviceName?uncap_first}.delete${entity}(${field.propertyName});
        return new CommonResponse();
    }

    @ApiOperation(value = "改")
    @PutMapping(value = "{${field.propertyName}}")
    public CommonResponse update(@PathVariable("${field.propertyName}") ${field.propertyType} ${field.propertyName}, @Valid @RequestBody ${entity}DTO dto){
        dto.set${field.propertyName?cap_first}(${field.propertyName});
        ${table.serviceName?uncap_first}.update${entity}(dto);
        return new CommonResponse();
    }

    @ApiOperation(value = "查")
    @GetMapping(value = "{${field.propertyName}}")
    public CommonResponse<${entity}VO> info(@PathVariable("${field.propertyName}") ${field.propertyType} ${field.propertyName}){
        return new CommonResponse<>(${table.serviceName?uncap_first}.get${entity}ById(${field.propertyName}));
    }
    </#if>
</#list>

    @ApiOperation(value = "分页")
    @GetMapping(value = "")
    public CommonResponse<PageVO<${entity}VO>> page(@Valid ${entity}PageDTO pageDTO){
        return new CommonResponse<>(${table.serviceName?uncap_first}.get${entity}ListByPage(pageDTO));
    }

}