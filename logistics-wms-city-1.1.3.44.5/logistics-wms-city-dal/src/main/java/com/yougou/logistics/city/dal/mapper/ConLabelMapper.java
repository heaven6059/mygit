/*
 * 类名 com.yougou.logistics.city.dal.mapper.ConLabelMapper
 * @author qin.dy
 * @date  Mon Sep 30 15:09:38 CST 2013
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
package com.yougou.logistics.city.dal.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.ConLabel;

public interface ConLabelMapper extends BaseCrudMapper {
	
	/**
	 * 修改标签表的状态
	 * @param conLabel
	 * @return
	 * @throws DaoException
	 */
	public int  modifyStatusByLocnoAndLabelNo(ConLabel conLabel)throws DaoException;
	
	public void deleteConLabel4Recheck(@Param("params") Map<String, Object> params) throws DaoException;
}