package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
public interface BillUmUntreadMmManager extends BaseCrudManager {
	/**
	 * 保存主档
	 * @param untreadMm
	 */
	public void saveMain(BillUmUntreadMm untreadMm) throws ManagerException;

	/**
	 * 删除
	 * @param untreadMm
	 * @throws ManagerException
	 */
	public void deleteUntread(String keyStr, String locnoNo) throws ManagerException;

	/**
	 * 审核
	 * @param untreadMm
	 * @throws ManagerException
	 */
	public void auditUntread(String keyStr, String locno, String oper) throws ManagerException;
}