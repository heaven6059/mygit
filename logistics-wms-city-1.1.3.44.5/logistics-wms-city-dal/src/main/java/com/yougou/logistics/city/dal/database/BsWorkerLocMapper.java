/*
 * 类名 com.yougou.logistics.city.dal.mapper.BsWorkerLocMapper
 * @author luo.hl
 * @date  Mon Sep 23 10:25:26 CST 2013
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BmDefloc;

public interface BsWorkerLocMapper extends BaseCrudMapper {
	/**
	 * 删除用户下的所有仓库
	 * @param obj
	 * @return
	 */
	public abstract int deleteByWorkerNo(Object obj);

	/**
	 * 
	 * 通过用户编码获取仓别
	 * @param params
	 * @return
	 */
	public List<BmDefloc> selectLocByWorkerNo(@Param("params") Map<String, Object> params) throws DaoException;
}