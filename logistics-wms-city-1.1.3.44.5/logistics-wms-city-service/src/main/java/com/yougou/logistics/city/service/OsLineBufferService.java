package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.OsLineBuffer;
import com.yougou.logistics.city.common.model.Store;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Sep 27 09:54:25 CST 2013
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
public interface OsLineBufferService extends BaseCrudService {

	List<Store> getStoreInfo(String cellNo, String locno)throws ServiceException;
	
	public List<OsLineBuffer>  selectBufferBySupplierNo(OsLineBuffer osLineBuffer) throws ServiceException;
}