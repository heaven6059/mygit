package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportKey;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 10:24:56
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
public interface BillImImportMapper extends BaseCrudMapper {

	/**
	 * 根据主键删除数据
	 * @param billImImportDtlKey
	 * @return
	 * @throws DaoException
	 */
	public int deleteByPrimaryKey(BillImImportKey billImImportKey) throws DaoException;
	
	/**
	 * 根据主键删除数据
	 * @param billImImport
	 * @return
	 * @throws DaoException
	 */
	public int deleteByPrimarayKeyForModel(BillImImport billImImport)throws DaoException;

	public int selectImpoertNoCount(@Param("params") Map map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<?> selectImportNoByPage(@Param("page") SimplePage page, @Param("params") Map map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询已经审核的通知单的供应商
	 * @param imp
	 * @return
	 */
	public List<Supplier> selectSupplierNo(@Param("dto")BillImImport imp,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	

	public int selectVerFlocByReceipt(BillImImport billImImport) throws DaoException;
	
	public int selectVerFlocByDivide(BillImImport billImImport) throws DaoException;
	
	public int selectVerFlocByCheck(BillImImport billImImport) throws DaoException;
	
	public Map<String, Object> selectSumQty(@Param("params") Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams);
}