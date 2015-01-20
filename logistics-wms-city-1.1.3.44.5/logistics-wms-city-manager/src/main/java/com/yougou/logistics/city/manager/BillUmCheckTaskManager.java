package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmUntread;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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
public interface BillUmCheckTaskManager extends BaseCrudManager {

	/**
	 * 新建退仓验收任务
	 * @param untreadList
	 * @throws ServiceException
	 */
	public void saveUmCheckTask(BillUmCheckTask umCheckTask, List<BillUmUntread> untreadList) throws ManagerException;

	/**
	 * 删除退仓验收任务
	 * @param taskList
	 * @throws ServiceException
	 */
	public void deleteUmCheckTask(List<BillUmCheckTask> taskList) throws ManagerException;
	
	/**
	 * 删除退仓验收任务
	 * @param keyStr
	 * @param loginName
	 * @param userName
	 * @param locno
	 * @throws ManagerException
	 */
	public void  auditUmCheckTask(String keyStr,String loginName,String userName,String locno)throws ManagerException;
    /**
     * 退仓验收任务合计
     * @param params
     * @param authorityParams
     * @return
     */
    public Map<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams)throws ManagerException;
}