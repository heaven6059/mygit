/*
 * 类名 com.yougou.logistics.city.dal.database.ConContentMoveMapper
 * @author yougoupublic
 * @date  Fri Mar 07 11:21:04 CST 2014
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
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.ConContentMove;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface ConContentMoveMapper extends BaseCrudMapper {

    public SumUtilMap<String, Object> selectSumQty(
	    @Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams);

    public List<ConContentMove> selectConContentMoveBoxNo(
	    @Param("params") Map<String, Object> param);
}