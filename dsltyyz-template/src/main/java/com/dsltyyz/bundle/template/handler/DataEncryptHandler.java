package com.dsltyyz.bundle.template.handler;

import com.dsltyyz.bundle.template.util.AesUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据加密解密处理类
 *
 * @author dsltyyz
 */
public class DataEncryptHandler extends BaseTypeHandler<Object> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, AesUtils.encrypt(String.valueOf(o)));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        return value == null ? null : AesUtils.decrypt(value);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        return value == null ? null : AesUtils.decrypt(value);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        return value == null ? null : AesUtils.decrypt(value);
    }

}
