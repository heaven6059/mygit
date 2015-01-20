package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Sep 25 14:31:21 CST 2013
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
public interface BsWorkerAreaManager extends BaseCrudManager {
	/**
	 * 批量删除人员区域关系
	 * @param keyStr
	 * @return
	 * @throws ManagerException
	 */
	public int deleteBsWorkerAreaBatch(String keyStr) throws ManagerException;
}