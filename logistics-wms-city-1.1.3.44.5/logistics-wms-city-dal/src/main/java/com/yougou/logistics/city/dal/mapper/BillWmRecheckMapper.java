package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.Supplier;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
public interface BillWmRecheckMapper extends BaseCrudMapper {
	
	public int  countLabelNoByRecheckNo(@Param("params") BillWmRecheck vo,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	public List<BillWmRecheck>  findLabelNoByRecheckNoPage(@Param("page") SimplePage page,@Param("params") BillWmRecheck vo, @Param("authorityParams") AuthorityParams authorityParams)throws DaoException;

	public List<Supplier> querySupplier(@Param("locno") String locno )throws DaoException;

	public List<BillWmOutstockDtl> queryRecheckItem(@Param("params") Map<String, Object> params)throws DaoException;

	public void procRecheckPackageBox(Map<String, String> map) throws DaoException;
	
	public void procRecheckComplete(Map<String, String> map) throws DaoException;
	/**
	 * 删除拣货复核单调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procWmOutStockRecheckDel(Map<String, String> map) throws DaoException;
}