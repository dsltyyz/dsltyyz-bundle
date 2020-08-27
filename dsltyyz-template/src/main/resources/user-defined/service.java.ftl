package ${package.Service};

import ${package.Entity}.*;
import ${package.Entity?replace('entity','dto')}.*;
import ${package.Entity?replace('entity','vo')}.*;
import com.dsltyyz.bundle.common.page.*;
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * 新建
     * @param dto
     */
    void save${entity}(${entity}DTO dto);

    /**
     * 更新
     * @param dto
     */
    void update${entity}(${entity}DTO dto);

    /**
     * 删除
     * @param id
     */
    void delete${entity}(Long id);

    /**
     * 查询
     * @param id
     * @return
     */
    ${entity}VO findById(Long id);

    /**
     * 分页查询
     * @param pageDTO
     * @return
     */
    PageVO<${entity}VO> findAllByPage(${entity}PageDTO pageDTO);
}