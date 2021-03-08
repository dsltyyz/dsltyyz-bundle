package ${package.Mapper};

import ${package.Entity}.${entity};
import com.dsltyyz.bundle.common.mybatisplus.dao.DsltyyzDAO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ${table.comment!} Dao
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper
public interface ${table.mapperName} extends DsltyyzDAO<${entity}> {

}