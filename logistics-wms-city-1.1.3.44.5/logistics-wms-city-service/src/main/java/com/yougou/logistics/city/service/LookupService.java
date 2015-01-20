package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

public interface LookupService extends BaseCrudService {
	
	
	public int checkItemValue(String itemval,String lookupcode,int systemid) throws ServiceException;
	
	
	
	public void deletelookup(String lookupcode)throws ServiceException;
	
	
	public int checkLookuoCode(String lookupcode)throws ServiceException;
}