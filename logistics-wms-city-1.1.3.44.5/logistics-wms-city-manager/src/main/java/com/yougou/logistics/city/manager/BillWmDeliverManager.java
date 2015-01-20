package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillWmDeliver;

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
public interface BillWmDeliverManager extends BaseCrudManager {
	
	public boolean deleteWmDeliver(String nos)throws ManagerException;
	
	public Map<String,Object> addWmDeliver(BillWmDeliver billWmDeliver)throws ManagerException;
	
	public Map<String, Object>  auditWmDeliver(String noStrs,String locno,String loginName,String userName)throws ManagerException;
	
}