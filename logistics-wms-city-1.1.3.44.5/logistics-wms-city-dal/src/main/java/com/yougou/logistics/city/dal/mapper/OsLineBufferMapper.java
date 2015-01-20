/*
 * 类名 com.yougou.logistics.city.dal.mapper.OsLineBufferMapper
 * @author luo.hl
 * @date  Fri Sep 27 09:54:25 CST 2013
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

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.OsLineBuffer;
import com.yougou.logistics.city.common.model.Store;

public interface OsLineBufferMapper extends BaseCrudMapper {
	
	/**
	 * 根据发货区和仓别查询客户信息
	 * @param cellNo发货区
	 * @param locno仓别
	 * @return
	 */
	public List<Store> getStoreInfo(@Param("cellNo")String cellNo, @Param("locno")String locno);
	
	
	public List<OsLineBuffer>  selectBufferBySupplierNo(OsLineBuffer osLineBuffer) throws DaoException;
	
}