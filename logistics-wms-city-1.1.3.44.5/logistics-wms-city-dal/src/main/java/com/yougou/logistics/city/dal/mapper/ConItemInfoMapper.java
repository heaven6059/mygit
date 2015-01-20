/*
 * 类名 com.yougou.logistics.city.dal.mapper.ConItemInfoMapper
 * @author qin.dy
 * @date  Sat Nov 09 16:23:50 CST 2013
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

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface ConItemInfoMapper extends BaseCrudMapper {
	public String getItemId(@Param("itemNo")String itemNo,@Param("barcode")String barcode,@Param("supplierNo")String supplierNo,@Param("importNo")String importNo);
	public String getMaxItemIdByItemNo(@Param("itemNo")String itemNo);
}