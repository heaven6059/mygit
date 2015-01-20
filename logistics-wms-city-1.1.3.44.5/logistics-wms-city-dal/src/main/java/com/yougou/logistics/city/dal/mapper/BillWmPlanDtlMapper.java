/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillWmPlanDtlMapper
 * @author yougoupublic
 * @date  Fri Mar 21 13:37:10 CST 2014
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
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.Item;

public interface BillWmPlanDtlMapper extends BaseCrudMapper {

    public List<Item> selectItem(@Param("page") SimplePage page,
	    @Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

    public int selectItemCount(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    public void updateAllDetail(BillWmPlan plan);
}