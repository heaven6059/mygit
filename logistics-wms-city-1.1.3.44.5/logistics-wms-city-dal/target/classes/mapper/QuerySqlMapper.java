package com.yougou.logistics.lsp.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface QuerySqlMapper {
	List<Map<String, Object>> querySql(@Param("sql") String sql);
}