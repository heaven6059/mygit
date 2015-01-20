package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AccTask;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-23 18:31:36
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface AccTaskMapper extends BaseCrudMapper {
	
	/**
	 * 查询最近一条
	 * @param list
	 * @throws DaoException
	 */
	public AccTask selectLatestOneByParams(@Param("params")Map<String,Object> params) throws DaoException;
	
	/**
	 * 查询未处理的任务一条整单及全部明细
	 * @param list
	 * @throws DaoException
	 */
	public List<AccTask> selectLatestListByAccType(@Param("params")Map<String,Object> params) throws DaoException;
	/**
	 * 加锁更新
	 * @param params
	 */
	public void updateLockForAccTask(AccTask accTask) throws DaoException;
	
	/**
	 * 按参数billNo,billType,ioFlag查询
	 * @param accTask
	 */
	public AccTask selectByBillPrimaryKey(AccTask accTask) throws DaoException;
}