/*
 * 类名 com.yougou.logistics.city.dal.database.BillLocateRuleDtlMapper
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.model.Category;

public interface BillLocateRuleDtlMapper extends BaseCrudMapper {
	
	/**
	 * 过滤删除的明细
	 * @param listLocateRuleDtls
	 * @return
	 */
	public List<BillLocateRuleDtl> selectBillLocateRuleDtlFilter(@Param("params")Map<String,Object> params,@Param("list")List<BillLocateRuleDtl> listLocateRuleDtls) throws DaoException;
	/**
	 * 查询过滤后的商品类别分页总数
	 * @param listCategorys
	 * @return
	 */
	public int selectCategoryFilterCount(@Param("params")Map<String,Object> params,@Param("list")List<Category> listCategorys) throws DaoException;
	
	/**
	 * 查询过滤后的商品类别分页
	 * @param listCategorys
	 * @return
	 * @throws DaoException
	 */
	public List<Category> selectCategoryFilterByPage(@Param("page") SimplePage page,@Param("params")Map<String,Object> params,@Param("list")List<Category> listCategorys) throws DaoException;
	
}