package com.yougou.logistics.city.manager;

import java.util.List;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillWmOutstock;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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
public interface BillWmOutstockDirectManager extends BaseCrudManager {
	
	/**
	 * 退厂拣货下架指示表发单
	 * @param listDirects
	 * @throws ServiceException
	 */
	public void sendWmOutstockDirect(BillWmOutstock billWmOutstock,List<BillWmOutstockDirect> listDirects) throws ManagerException;
	
}