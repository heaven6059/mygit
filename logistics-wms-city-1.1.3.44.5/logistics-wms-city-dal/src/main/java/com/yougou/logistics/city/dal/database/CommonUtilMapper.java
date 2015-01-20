package com.yougou.logistics.city.dal.database;

import java.util.List;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
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
public interface CommonUtilMapper extends BaseCrudMapper{
	
	
	/**
	 * 根据字典分类编号查询字典明细
	 * @param lookupDtl
	 * @return
	 */
	public List<LookupDtl> queryLookupDtlsList(LookupDtl lookupDtl) throws DaoException;
	
	/**
	 * 根据模块ID查询中文显示字段
	 * @param vo
	 * @return
	 * @throws DaoException
	 */
	public List<Column> queryCommonQueryConfig(Column vo) throws DaoException;
	
}