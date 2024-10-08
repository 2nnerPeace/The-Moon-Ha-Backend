package com.innerpeace.themoonha.global.handler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * List & 오라클 Array 타입 호환 핸들러
 * @author 최유경
 * @since 2024.08.31
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.31  	최유경       최초 생성
 * </pre>
 */
public class StringListTypeHandler extends BaseTypeHandler<List<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> strings, JdbcType jdbcType)
            throws SQLException {
        Connection conn = ps.getConnection();
        // 리스트를 배열로 변환
        String[] stringArray = strings.toArray(new String[0]);

        // Oracle의 커스텀 타입으로 배열 생성
        Array oracleArray = conn.unwrap(oracle.jdbc.OracleConnection.class)
                .createOracleArray("VARCHAR2_TABLE", stringArray);

        // PreparedStatement에 배열을 설정
        ps.setArray(i, oracleArray);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String s) throws SQLException {
        return null;
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int i) throws SQLException {
        return null;
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int i) throws SQLException {
        return null;
    }
}
