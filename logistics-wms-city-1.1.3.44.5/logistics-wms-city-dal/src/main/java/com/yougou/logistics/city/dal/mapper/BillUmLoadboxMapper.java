/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillUmLoadboxMapper
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
 * @version 1.0.6
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

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmLoadbox;

public interface BillUmLoadboxMapper extends BaseCrudMapper {
	public void updateAllDetail(@Param("params") BillUmLoadbox box) throws DaoException;
	
	public String  selectQuaByLoadBoxNo(String loadboxNo)throws DaoException;
}