package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillWmDeliverDtl;
import com.yougou.logistics.city.common.model.BillWmDeliverDtlKey;
import com.yougou.logistics.city.common.model.ConLabelDtl;
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
public interface BillWmDeliverDtlManager extends BaseCrudManager {
	
	public List<ConLabelDtl> getLabelInfoDtlsList(List<BillWmDeliverDtl> lstCnBoxQueryDto)throws ManagerException;
	
	public <ModelType> boolean  addWmDeliverDetail(String locno,String ownerNo,String supplierNo,String deliverNo,Map<CommonOperatorEnum, List<ModelType>>  params,String LoginName,String userChName)throws ManagerException;
	
	public int countWmDeliverDtlByMainId(BillWmDeliverDtlKey  vo )throws ManagerException;
	
	public List<BillWmDeliverDtl> findWmDeliverDtlByMainIdPage(SimplePage page,BillWmDeliverDtlKey  vo )throws ManagerException;
	
	public Map<String,Object> validateLabelNo(String  noStrs,String deliverNo,String locno) throws ManagerException;
	
	/**
	 * 查询退厂确认单汇总箱号明细分页总数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findBillWmDeliverDtlGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询退厂确认单汇总箱号明细分页
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmDeliverDtl> findBillWmDeliverDtlGroupByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	
	/**
	 * 查询退厂确认单明细分页总数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findBillWmDeliverDtlCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询退厂确认单明细分页
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmDeliverDtl> findBillWmDeliverDtlByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
}