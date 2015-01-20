package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillUmLoadboxService extends BaseCrudService {
	
	public void createLoadBox(String strKey, BillUmUntreadMm untreadMm, SystemUser user) throws ServiceException;

	public void auditLoadBox(String keyStr, String locno, String oper) throws ServiceException;
	
	public String selectQuaByLoadBoxNo(String loadboxNo)throws ServiceException;
}