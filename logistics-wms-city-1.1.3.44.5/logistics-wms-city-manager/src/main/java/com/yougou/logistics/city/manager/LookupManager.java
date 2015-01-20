package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

public interface LookupManager extends BaseCrudManager {
	
	public int checkItemValue(String itemval, String lookupcode, int systemid) throws ManagerException;
	
	
	public void deletelookup(String lookupcode)throws ManagerException;
	
	
	public int checkLookuoCode(String lookupcode)throws ManagerException;
}