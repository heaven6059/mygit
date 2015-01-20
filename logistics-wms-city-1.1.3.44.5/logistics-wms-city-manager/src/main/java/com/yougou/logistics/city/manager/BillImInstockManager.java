package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillImInstock;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 30 09:51:28 CST 2013
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
public interface BillImInstockManager extends BaseCrudManager {
	/**
	 * 审核
	 * @param instock
	 * @return
	 * @throws ManagerException
	 */
	public Map<String, Object> audit(BillImInstock instock) throws ManagerException;
}