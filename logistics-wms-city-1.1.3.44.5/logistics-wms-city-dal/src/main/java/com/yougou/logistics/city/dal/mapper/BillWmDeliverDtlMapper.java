package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillWmDeliverDtl;
import com.yougou.logistics.city.common.model.BillWmDeliverDtlKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 10:44:50
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
public interface BillWmDeliverDtlMapper extends BaseCrudMapper {
	
	/**
	 * 查询退厂配送单的所有箱号
	 * @param billWmDeliverDtlKey
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmDeliverDtl> selectBoxNoByDetail(BillWmDeliverDtlKey billWmDeliverDtlKey)throws DaoException;
	
	public int selectMaxPoId(BillWmDeliverDtlKey billWmDeliverDtlKey)throws DaoException;
	
	public int countWmDeliverDtlByMainId(@Param("params") BillWmDeliverDtlKey  vo )throws DaoException;
	
	public List<BillWmDeliverDtl> findWmDeliverDtlByMainIdPage(@Param("page") SimplePage page,@Param("params") BillWmDeliverDtlKey  vo )throws DaoException;
	
	public int selectDeliverDtl(BillWmDeliverDtl billWmDeliverDtl)throws DaoException;
	
	public List<BillWmDeliverDtl> selectDeliverDtlByLabelNo(@Param("params")Map<String, Object> maps) throws DaoException;
	
	/**
	 * 查询退厂确认单汇总箱号明细分页总数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectBillWmDeliverDtlGroupByCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询退厂确认单汇总箱号明细分页
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmDeliverDtl> selectBillWmDeliverDtlGroupByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	
	/**
	 * 查询退厂确认单明细分页总数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectBillWmDeliverDtlCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询退厂确认单明细分页
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmDeliverDtl> selectBillWmDeliverDtlByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public void updateOperateRecord(Map<String, Object> map)throws DaoException;
}