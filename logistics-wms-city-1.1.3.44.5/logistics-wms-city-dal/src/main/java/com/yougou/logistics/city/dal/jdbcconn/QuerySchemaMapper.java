package com.yougou.logistics.city.dal.jdbcconn;

import java.util.List;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.city.common.vo.Column;
/**
 * 查询Schema
 * @author wei.hj
 * @date 2013-07-15
 * @version 0.1.0
 * @copyright yougou.com
 *
 */
public interface QuerySchemaMapper {
	/**
	 * 查询Schema   注意：在使用过程中，参数名称必须使用大写的。
	 * @param catalog  目录名称，一般都为空
	 * @param schema   数据库用户名
	 * @param tableName  所查表名
	 * @param type  类型  表的类型(TABLE | VIEW)
	 * @throws DaoException
	 */
	public List<Column> selectDataBaseMetaData(String catalog,String schema,String tableName,String[]type) throws DaoException;
	
	
}
