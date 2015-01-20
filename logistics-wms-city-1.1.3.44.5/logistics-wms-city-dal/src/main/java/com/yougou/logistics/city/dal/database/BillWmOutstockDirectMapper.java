/*
 * 类名 com.yougou.logistics.city.dal.database.BillWmOutstockDirectMapper
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;

public interface BillWmOutstockDirectMapper extends BaseCrudMapper {
	/**
	 * 更新下架指示表
	 * @param direct
	 * @return
	 * @throws DaoException
	 */
	public int updateBillWmOutstockDirect(BillWmOutstockDirect direct) throws DaoException; 
	
	/**
	 * 查询是否全部拣货完成
	 * @param direct
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmOutstockDirect> selectBillWmOutstockDirectAndOutstockDtl(@Param("params")Map<String,Object> params) throws DaoException; 
	
}