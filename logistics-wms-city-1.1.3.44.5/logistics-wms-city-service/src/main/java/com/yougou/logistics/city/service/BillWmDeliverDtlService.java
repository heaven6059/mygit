package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
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
public interface BillWmDeliverDtlService extends BaseCrudService {
	
	/**
	 * 查询退厂配送单的所有箱号
	 * @param billWmDeliverDtlKey
	 * @return
	 * @throws ServiceException
	 */
	public List<BillWmDeliverDtl> selectBoxNoByDetail(BillWmDeliverDtlKey billWmDeliverDtlKey)throws ServiceException;
	
	/**
	 * 查询最大的poid
	 * @param billWmDeliverDtlKey
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxPid(BillWmDeliverDtlKey billWmDeliverDtlKey)throws ServiceException;
	
	public int countWmDeliverDtlByMainId(BillWmDeliverDtlKey  vo )throws ServiceException;
	
	public List<BillWmDeliverDtl> findWmDeliverDtlByMainIdPage(SimplePage page,BillWmDeliverDtlKey  vo )throws ServiceException;
	
	public int selectCountLabelNo(String deliverNo,String labelNo)throws ServiceException;
	
	public int selectDeliverDtl(BillWmDeliverDtl billWmDeliverDtl)throws ServiceException;
	
	public List<BillWmDeliverDtl> selectDeliverDtlByLabelNo(Map<String, Object> maps) throws ServiceException;
	
	/**
	 * 查询退厂确认单汇总箱号明细分页总数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findBillWmDeliverDtlGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询退厂确认单汇总箱号明细分页
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmDeliverDtl> findBillWmDeliverDtlGroupByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	
	/**
	 * 查询退厂确认单明细分页总数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findBillWmDeliverDtlCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询退厂确认单明细分页
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmDeliverDtl> findBillWmDeliverDtlByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams)throws ServiceException;

	public void updateOperateRecord(Map<String, Object> map) throws ServiceException;
}