/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillUmReceiptDtlMapper
 * @author luo.hl
 * @date  Mon Jan 13 20:08:07 CST 2014
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

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmReceiptDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillUmReceiptDtlMapper extends BaseCrudMapper {
	/**
	 * 
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public int selectCountByBox(@Param("params")Map<String,Object> params) throws DaoException;
	/**
	 * 查询明细表中的箱子
	 * @param page
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillUmReceiptDtl> selectByPageByBox(@Param("page") SimplePage page,@Param("params")Map<String,Object> params) throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
}