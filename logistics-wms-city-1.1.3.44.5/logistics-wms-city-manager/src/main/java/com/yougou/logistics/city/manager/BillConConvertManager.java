package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
public interface BillConConvertManager extends BaseCrudManager {
	
	/**
	 * 删除主档和明细
	 * @param keyStr locno_ownerNo_convertNo用|隔开
	 * @return
	 * @throws ManagerException
	 */
	public String removeMainAndDtl(String keyStr) throws ManagerException;
	/**
	 * 审核
	 * @param keyStr locno_ownerNo_convertNo_storeNo用|隔开
	 * @param operator 操作人(审核人)
	 * @param type 0跨部门审核  1转货单跨部门审核
	 * @return
	 * @throws ManagerException
	 */
	public String check(String keyStr,String operator,int type,String storeNoLoc,String auditorName) throws ManagerException;
	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
}