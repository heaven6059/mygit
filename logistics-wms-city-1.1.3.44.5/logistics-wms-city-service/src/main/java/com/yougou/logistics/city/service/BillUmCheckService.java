package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.SystemUser;
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
public interface BillUmCheckService extends BaseCrudService {

	/**
	 * 审核退仓收货单存储过程
	 * @param map
	 * @throws DaoException
	 */
	public Map<String, Object> procBillUmCheckAuditQuery(List<BillUmCheck> listBillUmChecks) throws ServiceException;

	public int selectCountUmNoForInstock(Map map) throws ServiceException;

	/**
	 * 保存主表
	 * @param check
	 * @throws ServiceException
	 */
	public void saveMain(BillUmCheck check) throws ServiceException;

	public void deleteCheck(String keyStr, String locnoNo) throws ServiceException;

	public void auditCheck(String keyStr, SystemUser user) throws ServiceException;

	public List<BillUmCheck> selectByPageUmNoForInstock(SimplePage page, Map map) throws ServiceException;

	public int selectCountCheckNoForInstock(Map map, AuthorityParams authorityParams) throws ServiceException;

	public List<BillUmCheck> selectByPageCheckNoForInstock(SimplePage page, Map map, AuthorityParams authorityParams)
			throws ServiceException;

	public int selectCount4loadBox(Map map, AuthorityParams authorityParams) throws ServiceException;

	public List<BillUmCheck> select4loadBoxByPage(Map map, SimplePage page, AuthorityParams authorityParams)
			throws ServiceException;

	public SumUtilMap<String, Object> selectUntreadJoinCheckDtlSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException;

	public SumUtilMap<String, Object> selectUntreadJoinCheckSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException;
	
	public int findBillUmCheckCount(Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException;

	public List<BillUmCheck> findBillUmCheckByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
}