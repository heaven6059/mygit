package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 23 10:24:36 CST 2013
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
public interface BmDefdockManager extends BaseCrudManager {
	/**
	 * 批量删除码头信息
	 * @param keyStr
	 * @return
	 */
	public int deleteBmDefdockBatch(String keyStr) throws ManagerException;
}