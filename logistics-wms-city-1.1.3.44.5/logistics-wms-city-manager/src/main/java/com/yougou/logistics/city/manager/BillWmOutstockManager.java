package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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
public interface BillWmOutstockManager extends BaseCrudManager {

	/**
	 * 拣货确认
	 * @param locno
	 * @param outstockNo
	 * @param keyStr
	 * @param ownerNo TODO
	 * @param userChName 当前登录用户的中文名称
	 * @throws ManagerException
	 */
	public void checkOutstock(String locno, String outstockNo, String outstockName, String keyStr, String ownerNo,String userChName)
			throws ManagerException;
}