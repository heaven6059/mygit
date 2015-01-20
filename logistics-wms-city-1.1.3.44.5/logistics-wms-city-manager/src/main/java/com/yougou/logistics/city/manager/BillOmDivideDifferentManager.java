package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmDivideDifferent;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;

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
public interface BillOmDivideDifferentManager extends BaseCrudManager {

	/**
	 * 转商品差异调整
	 * @param divideDtlList
	 * @param loginName
	 * @throws ManagerException
	 */
	public void toDivideDifferent(List<BillOmDivideDtl> divideDtlList, String loginName,String userName) throws ManagerException;

	/**
	 * 删除差异调整单
	 * @param divideDtlList
	 * @param loginName
	 * @throws ManagerException
	 */
	public void delDivideDifferent(List<BillOmDivideDifferent> differentList, String loginName) throws ManagerException;

	/**
	 * 审核差异调整单
	 * @param divideDtlList
	 * @param loginName
	 * @throws ManagerException
	 */
	public void auditDivideDifferent(List<BillOmDivideDifferent> differentList, String loginName,String userName)
			throws ManagerException;
}