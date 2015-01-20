package com.yougou.logistics.lsp.dal.database;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("schemaAndProDAO")
public class SchemaAndProDAO {
	public Map<String, Object> executeSchema(String sql) throws Exception {
		sql = "select * from ( " + sql + " )  A";
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("column", map);
		returnMap.put("rows", lt);
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory()
				.openSession();
		Connection conn = sqlSession.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.execute();
		ResultSetMetaData rsm = pst.getMetaData();
		for (int i = 1; i <= rsm.getColumnCount(); i++) {
			map.put(rsm.getColumnName(i), rsm.getColumnTypeName(i));
		}
		ResultSet rs = pst.getResultSet();
		while (rs.next()) {
			Map<String, Object> row = new LinkedHashMap<String, Object>();
			lt.add(row);
			Iterator<Map.Entry<String, String>> keys = map.entrySet()
					.iterator();
			while (keys.hasNext()) {
				Map.Entry<String, String> entry = keys.next();
				String key = entry.getKey();
				String value = entry.getValue();
				if (value.equals("VARCHAR2")) {
					row.put(key, rs.getString(key));
				}
				if (value.equals("NUMBER")) {
					row.put(key, rs.getLong(key));
				}
				if (value.equals("DATE")) {
					row.put(key, rs.getDate(key));
				}
			}
			lt.add(row);
		}
		return returnMap;
	}

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public Map<String, Object> executeCall(String procedureName,
			List<Bean> beans) throws Exception {
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory()
				.openSession();
		Connection conn = sqlSession.getConnection();
		CallableStatement callStmt = null;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			StringBuffer sb = new StringBuffer("{ call ");
			sb.append(procedureName);
			sb.append("(");

			if (beans == null || beans.size() <= 0) {
				sb.append(")}");
				callStmt = conn.prepareCall(sb.toString());
				callStmt.execute();
				return null;
			}

			int length = beans.size();
			for (int i = 0; i < length; i++) {
				if (i > 0 && i < length) {
					sb.append(",");
				}
				sb.append("?");
			}
			sb.append(")}");
			callStmt = conn.prepareCall(sb.toString());

			for (int i = 0; i < beans.size(); i++) {
				Bean bean = beans.get(i);
				Object value = bean.getValue(); // 值
				Integer index = bean.getIndex(); // 索引
				Integer outype = bean.getOutType();
				Integer iostatus = bean.getIostatus();
				switch (iostatus) {
				case 0:
					call_In(callStmt, index, value);
					break;
				case 1:
					callStmt.registerOutParameter(index, outype);
					break;
				case 2:
					call_In(callStmt, index, value);
					callStmt.registerOutParameter(index, outype);
					break;
				default:
					break;
				}
			}
			callStmt.execute();

			for (int i = 0; i < length; i++) {
				Bean bean = beans.get(i);
				if (bean.getIostatus() > 0) {
					switch (bean.getOutType()) {
					case Types.INTEGER:
						int ivalue = callStmt.getInt(bean.getIndex());
						returnMap.put((String) bean.getValue(), ivalue);
						break;
					case Types.VARCHAR:
						String svalue = callStmt.getString(bean.getIndex());
						returnMap.put((String) bean.getValue(), svalue);
						break;
					case Types.DATE:
						Date date = callStmt.getDate(bean.getIndex());
						java.util.Date mydate = date;
						returnMap.put((String) bean.getValue(), mydate);
						break;

					case Types.BOOLEAN:
						Boolean bvalue = callStmt.getBoolean(bean.getIndex());
						returnMap.put((String) bean.getValue(), bvalue);
						break;
					case Types.BLOB:
						Blob blob = callStmt.getBlob(bean.getIndex());
						returnMap.put((String) bean.getValue(), blob);
					case OracleTypes.CURSOR:
						ResultSet rs = (ResultSet) callStmt.getObject(bean
								.getIndex());
						while (rs.next()) {

						}
					default:
						break;
					}
				}
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (callStmt != null) {
				callStmt.close();
			}
		}
		return returnMap;
	}

	private void call_In(CallableStatement callStmt, int index, Object value)
			throws SQLException {
		if (value instanceof String) {
			callStmt.setString(index, (String) value);
		}
		if (value instanceof Long) {
			callStmt.setLong(index, (Long) value);
		}
		if (value instanceof Double) {
			callStmt.setDouble(index, (Double) value);
		}
		if (value instanceof Integer) {
			callStmt.setInt(index, (Integer) value);
		}
		if (value instanceof Short) {
			callStmt.setShort(index, (Short) value);
		}
		if (value instanceof Date) {
			callStmt.setDate(index, (Date) value);
		}
		if (value instanceof Boolean) {
			callStmt.setBoolean(index, (Boolean) value);
		}
		if (value instanceof Byte) {
			callStmt.setByte(index, (Byte) value);
		}
		if (value instanceof Character) {
			callStmt.setString(index, String.valueOf(value));
		}

	}
}
