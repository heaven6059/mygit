package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
public interface BillOmPrepareDivideDtlManager extends BaseCrudManager {
	/**
	 * 详情数量
	 * 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int findPrepareDivideDetailCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 详情
	 * 
	 * @param page
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> findPrepareDivideDetail(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	public List<BillImReceiptDtl> selectItemDetail4Prepare(Map<String, Object> map, SimplePage page,AuthorityParams authorityParams) throws ManagerException;

	public int selectItemDetail4PrepareCount(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams);
}