package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BmLocnoGroup;

/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-11-07 10:46:51
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
public interface BmLocnoGroupManager extends BaseCrudManager {
	
	public Map<String, Object> addLocnoGroup(BmLocnoGroup bmLocnoGroup)throws ManagerException;
	
	public Map<String, Object> modifyLocnoGroup(BmLocnoGroup bmLocnoGroup)throws ManagerException;
	
	public Map<String, Object> deleteLocnoGroup(String locnogroupStrs) throws ManagerException;
	
}