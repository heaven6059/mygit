package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
public interface BillSmOtherinManager extends BaseCrudManager {
	
	public int deleteBatch(String ids) throws ManagerException;
	
	public Map<String, Object> checkBillSmOtherin(String ids, String auditor,String userName) throws ManagerException;
	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
	/**
	 * 打印
	 * @param nos 单号用逗号隔开
	 * @param locno
	 * @param user
	 * @return
	 */
	public Map<String, Object> print(String nos,String locno,SystemUser user);
}