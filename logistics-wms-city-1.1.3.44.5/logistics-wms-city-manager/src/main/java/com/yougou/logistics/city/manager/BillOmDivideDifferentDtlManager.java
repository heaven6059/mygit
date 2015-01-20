package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmDivideDifferent;
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
public interface BillOmDivideDifferentDtlManager extends BaseCrudManager {

	/**
	 * 明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 拆分明细
	 * @param differentDtl
	 * @throws ServiceException
	 */
	public void splitDifferentDtl(BillOmDivideDifferentDtl differentDtl) throws ManagerException;

	/**
	 * 保存明细
	 * @param different
	 * @param insertList
	 * @param updateList
	 * @param delList
	 * @throws ManagerException
	 */
	public void saveDifferentDtl(BillOmDivideDifferent different, List<BillOmDivideDifferentDtl> insertList,
			List<BillOmDivideDifferentDtl> updateList, List<BillOmDivideDifferentDtl> delList) throws ManagerException;
}