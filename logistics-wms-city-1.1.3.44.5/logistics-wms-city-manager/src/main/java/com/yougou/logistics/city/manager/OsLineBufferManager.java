package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface OsLineBufferManager extends BaseCrudManager {
	/**
	 * 批量删除暂存线路
	 * @param keyStr
	 * @return
	 * @throws ManagerException
	 */
	public int deleteOsLineBufferBatch(String keyStr) throws ManagerException;

	/**
	 * os_line_buffer  os_line_cust 根据这两个表查询客户信息
	 * @param cellNo
	 * @param locno 
	 * @return
	 * @throws ManagerException
	 */
	public List<Store> getStoreInfo(String cellNo, String locno)throws ManagerException;
}