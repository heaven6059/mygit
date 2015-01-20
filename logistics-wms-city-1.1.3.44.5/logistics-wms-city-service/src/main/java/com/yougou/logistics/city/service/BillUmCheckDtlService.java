package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 11 14:40:26 CST 2013
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
public interface BillUmCheckDtlService extends BaseCrudService {

	/**
	 * 退仓验收单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillUmCheckDtl> findBillUmCheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 退仓验收单汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findBillUmCheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;

	/**
	 * 查询商品是否存在
	 * @param billUmCheckDtl
	 * @return
	 * @throws DaoException
	 */
	public int selectItemNoIsHave(BillUmCheckDtl billUmCheckDtl) throws ServiceException;

	public Map<String, Object> saveCheckDtl(List<BillUmCheckDtl> insertList, List<BillUmCheckDtl> updateList,
			List<BillUmCheckDtl> deleteList, BillUmCheck check) throws ServiceException;

	/**
	 * 查询商品
	 * @param item
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public int selectItemCount4Check(Item item, AuthorityParams authorityParams) throws ServiceException;

	public List<Item> selectItem4Check(Item item, SimplePage page, AuthorityParams authorityParams) throws ServiceException;
	
	public int selectCountForDiff(Map map) throws ServiceException;
	
	public List<BillUmCheckDtl> selectByPageForDiff(SimplePage page, Map map) throws ServiceException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	public SumUtilMap<String, Object> selectPageSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException;
	/**
	 * 查询记录总数
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckDtl(BillCheckImRep model, AuthorityParams authorityParams);
	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model, AuthorityParams authorityParams);
	public int selectValidateUmCheckIsRecheck(Map<String, Object> params) throws ServiceException;
	public List<BillUmCheckDtl> selectCheckQtyJoinContent(Map<String, Object> params,List<BillUmCheck> list) throws ServiceException;
	public void updateRecheckQty4Convert(Map<String, Object> params) throws ServiceException;
	
}