package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmLocateDtlSizeKind;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 13:58:52 CST 2013
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
public interface BillOmLocateDtlManager extends BaseCrudManager {

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 批量打印拣货波次明细
	 * @param locno
	 * @param keys
	 * @return
	 * @throws ManagerException
	 */
	public List<String> printDetail(String locno, String keys, SystemUser user) throws ManagerException;

	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNoItemNoExpNo(Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException;

	public List<BillOmLocateDtlSizeKind> selectAllDtl4Print(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	public List<String> selectAllDtlSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNo(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;
}