package com.dsltyyz.bundle.template.mybatisplus;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 批量更新方法实现
 * @author zuop
 */
@Slf4j
public class UpdateBatchMethod extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\n<foreach collection=\"list\" item=\"item\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>";
        String additional = tableInfo.isWithVersion() ? tableInfo.getVersionFieldInfo().getVersionOli("item", "item.") : "" + tableInfo.getLogicDeleteSql(true, true);
        String setSql = sqlSet(tableInfo.isLogicDelete(), false, tableInfo, false, "item", "item.");
        String sqlResult = String.format(sql, tableInfo.getTableName(), setSql, tableInfo.getKeyColumn(), "item." + tableInfo.getKeyProperty(), additional);

        log.debug("sqlResult----->{}", sqlResult);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);

        // 第三个参数必须和RootMapper的自定义方法名一致
        return this.addUpdateMappedStatement(mapperClass, modelClass, "updateBatch", sqlSource);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        return "updateBatch";
    }
}
