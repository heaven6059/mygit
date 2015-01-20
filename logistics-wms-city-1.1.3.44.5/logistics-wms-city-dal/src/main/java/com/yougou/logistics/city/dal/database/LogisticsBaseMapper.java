package com.yougou.logistics.city.dal.database;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface LogisticsBaseMapper extends BaseCrudMapper{
	
	/**
	 * 执行存储过程
	 * 
	 * @param procedureName
	 * @throws DaoException
	 */
	public void executeProcedure(@Param("procedureName") String procedureName) throws DaoException;
}