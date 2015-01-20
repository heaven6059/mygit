/*
 * 类名 com.yougou.logistics.city.dal.database.BillConConvertMapper
 * @author su.yq
 * @date  Thu Jun 05 10:47:51 CST 2014
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

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface BillConConvertMapper extends BaseCrudMapper {
	public Map<String, Object> selectSumQty(@Param("params") Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams);
}