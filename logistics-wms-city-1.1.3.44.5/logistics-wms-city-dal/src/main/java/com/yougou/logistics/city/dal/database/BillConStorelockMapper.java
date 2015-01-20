/*
 * 类名 com.yougou.logistics.city.dal.database.BillConStorelockMapper
 * @author yougoupublic
 * @date  Sat Mar 08 11:25:53 CST 2014
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

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillConStorelock;

public interface BillConStorelockMapper extends BaseCrudMapper {
	
	/**
	 * 修改状态
	 * @param billConStorelock
	 * @return
	 * @throws DaoException
	 */
	public int updateStockStatus(BillConStorelock billConStorelock) throws DaoException;

	/**
	 * 库存冻结单合计
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public Map<String, Object> selectSumQty(@Param("params")Map<String, Object> params,
			@Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
	
}