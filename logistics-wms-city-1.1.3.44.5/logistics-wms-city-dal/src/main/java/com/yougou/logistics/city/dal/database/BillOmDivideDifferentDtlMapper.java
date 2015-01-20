package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmDivideDifferentDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
public interface BillOmDivideDifferentDtlMapper extends BaseCrudMapper {

	/**
	 * 查询差异单明细需要释放预下的商品
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDifferentDtl> selectDifferentDtlGroupByItem(@Param("params") Map<String, Object> params)
			throws DaoException;

	/**
	 * 明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询最大的rowId
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int selectMaxRowId(BillOmDivideDifferentDtl differentDtl) throws DaoException;
	
	/**
	 * 根据是否拆分查询差异调整单数据
	 * @param differentDtl
	 * @return
	 * @throws DaoException
	 */
	public BillOmDivideDifferentDtl selectDifferentByPixFlag(BillOmDivideDifferentDtl differentDtl) throws DaoException;
	
	/**
	 * 关联库存查询差异明细表
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDifferentDtl> selectDifferentDtl4Content(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 根据分货明细查询差异明细
	 * @param differentDtl
	 * @return
	 * @throws DaoException
	 */
	//public BillOmDivideDifferentDtl selectDtlByDivideDtl(BillOmDivideDifferentDtl differentDtl) throws DaoException;
}