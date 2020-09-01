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
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(value = "${table.comment!} controller", tags = {"${table.comment!}"})
@RestController
@RequestMapping("${package.ModuleName}/${entity?uncap_first}")
public class ${table.controllerName} {

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @ApiOperation(value = "增")
    @PostMapping(value = "")
    public CommonResponse create(@RequestBody ${entity}DTO dto){
        ${table.serviceName?uncap_first}.save${entity}(dto);
        return new CommonResponse();
    }

    @ApiOperation(value = "删")
    @DeleteMapping(value = "{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        ${table.serviceName?uncap_first}.delete${entity}(id);
        return new CommonResponse();
    }

    @ApiOperation(value = "改")
    @PutMapping(value = "{id}")
    public CommonResponse update(@PathVariable("id") Long id, @Valid @RequestBody ${entity}DTO dto){
        dto.setId(id);
        ${table.serviceName?uncap_first}.update${entity}(dto);
        return new CommonResponse();
    }

    @ApiOperation(value = "查")
    @GetMapping(value = "{id}")
    public CommonResponse<${entity}VO> info(@PathVariable("id") Long id){
        return new CommonResponse<>(${table.serviceName?uncap_first}.find${entity}ById(id));
    }

    @ApiOperation(value = "分页")
    @GetMapping(value = "")
    public CommonResponse<PageVO<${entity}VO>> page(@Valid ${entity}PageDTO pageDTO){
        return new CommonResponse<>(${table.serviceName?uncap_first}.find${entity}ByPage(pageDTO));
    }

}