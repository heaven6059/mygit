package com.yougou.logistics.city.manager.common;

import java.util.List;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.city.common.vo.Column;
import com.yougou.logistics.city.common.vo.LookupDtl;
/**
 * 系统公用处理
 * @author wei.hj
 * @date 2013-07-19
 * @version 0.1.0
 * @copyright yougou.com
 *
 */
public interface CommonUtilManager {
	/**
	 * 查询Schema   注意：在使用过程中，参数名称必须使用大写的。
	 * @param catalog  目录名称，一般都为空
	 * @param schema   数据库用户名
	 * @param tableName  所查表名
	 * @param type  类型  表的类型(TABLE | VIEW)
	 * @throws DaoException
	 */
	public List<Column> selectDataBaseMetaData(String catalog,String schema,String tableName,String[]type) throws ManagerException;

	
	/**
	 * 根据字典分类编号查询字典明细
	 * @param lookupDtl
	 * @return
	 */
	public List<LookupDtl> queryLookupDtlsList(LookupDtl lookupDtl) throws ManagerException;
	
	
	/**
	 * 根据模块ID查询中文显示字段
	 * @param vo
	 * @return
	 * @throws DaoException
	 */
	public List<Column> queryCommonQueryConfig(Column vo) throws ManagerException;
}
