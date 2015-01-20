/*
 * 类名 com.yougou.logistics.city.dal.database.ColorInfoMapper
 * @author su.yq
 * @date  Tue Oct 29 11:44:54 CST 2013
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

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.ColorInfo;

public interface ColorInfoMapper extends BaseCrudMapper {

//	public List<ColorInfo> findByPage1(SimplePage page, String sortColumn, String sortOrder, Map<String, Object> params,
//			@Param("authorityParams")AuthorityParams authorityParams);
//
//	public int findCount1(Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	
	
}