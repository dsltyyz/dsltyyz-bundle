package ${package.Mapper};

import ${package.Entity}.${entity};
import com.dsltyyz.bundle.template.mybatisplus.dao.DsltyyzDAO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ${table.comment!} Dao
 *
 * @author ${author}
 * @date ${date}
 */
@Mapper
public interface ${table.mapperName} extends DsltyyzDAO<${entity}> {

}