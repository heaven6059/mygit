package com.yougou.logistics.city.common.enums;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * jdbcType对照java类型对照表
 * 除Types.ARRAY外，其他都提供对照
 * @author wei.b
 *
 */
public enum JdbcTypeMappingEnum {
	BOOLEAN(Types.BOOLEAN,"boolean"),
	BIT(Types.BIT,"boolean"),
	BYTE(Types.TINYINT,"byte"),
	SMALLINT(Types.SMALLINT,"short"),
	INTEGER(Types.INTEGER,"int"),
	DOUBLE(Types.DOUBLE,"double"),
	CHAR(Types.CHAR,"String"),
	CLOB(Types.CLOB,"SVARCHARtring"),
	VARCHAR(Types.VARCHAR,"String"),
	LONGVHAR(Types.LONGVARCHAR,"String"),
	NVARCHAR(Types.NVARCHAR,"String"),
	NCHAR(Types.NCHAR,"String"),
	NCLOB(Types.NCLOB,"String"),
	BIGINT(Types.BIGINT,"long"),
	REAL(Types.REAL,"double"),
	DECIMAL(Types.DECIMAL,"double"),
	NUMERIC(Types.NUMERIC,"double"),
	BLOB(Types.BLOB,"byte[]"),
	LONGVARBINARY(Types.LONGVARBINARY,"byte"),
	DATE(Types.DATE,"Date"),
	TIME(Types.TIME,"Date"),
	TIMESTAMP(Types.TIMESTAMP,"Date");
	
	private final int jdbcType;
	private final String javaType;
	
	private static Map<Integer,String> javaTypeLookup = new HashMap<Integer,String>();
	private static Map<String,Integer> jdbcTypeLookup = new HashMap<String,Integer>();

	static {
		for (JdbcTypeMappingEnum type : JdbcTypeMappingEnum.values()) {
			javaTypeLookup.put(type.getJdbcType(),type.getJavaType());
		}
		jdbcTypeLookup.put(BOOLEAN.getJavaType(), BOOLEAN.getJdbcType());
		jdbcTypeLookup.put(BYTE.getJavaType(), BYTE.getJdbcType());
		jdbcTypeLookup.put(SMALLINT.getJavaType(), SMALLINT.getJdbcType());
		jdbcTypeLookup.put(INTEGER.getJavaType(), INTEGER.getJdbcType());
		jdbcTypeLookup.put(DOUBLE.getJavaType(), DOUBLE.getJdbcType());
		jdbcTypeLookup.put(VARCHAR.getJavaType(), VARCHAR.getJdbcType());
		jdbcTypeLookup.put(BIGINT.getJavaType(), BIGINT.getJdbcType());
		jdbcTypeLookup.put(DATE.getJavaType(), DATE.getJdbcType());
	}
	/**
	 * jdbc类型对照java类型对照表
	 * @param jdbcType 数据库对应JDBC类型
	 * @param javaType 对应Java类型
	 */
	JdbcTypeMappingEnum(int jdbcType,String javaType){
		this.jdbcType=jdbcType;
		this.javaType=javaType;
	}
	
	public int getJdbcType() {
		return jdbcType;
	}
	
	public String getJavaType() {
		return javaType;
	}
	
	public static String findJavaType(int jdbcType)  {
	    return javaTypeLookup.get(jdbcType);
	}
	
	public static Integer findJdbcType(String javaType)  {
		return jdbcTypeLookup.get(javaType);
	}
}