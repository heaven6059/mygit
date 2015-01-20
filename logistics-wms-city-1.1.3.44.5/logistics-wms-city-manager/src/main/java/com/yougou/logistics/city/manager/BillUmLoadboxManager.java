package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BillUmLoadboxManager extends BaseCrudManager {
	/**
	 * 生成装箱任务
	 * @param strKey
	 * @param untreadMm
	 * @throws ManagerException
	 */
	public void createLoadBox(String strKey, BillUmUntreadMm untreadMm, SystemUser user) throws ManagerException;

	public void auditLoadBox(String keyStr, String locno, String oper) throws ManagerException;
}