package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
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
public interface BillUmCheckTaskService extends BaseCrudService {
	
	/**
	 * 新建退仓验收任务
	 * @param untreadList
	 * @throws ServiceException
	 */
	public void saveUmCheckTask(BillUmCheckTask umCheckTask,List<BillUmUntread> untreadList) throws ServiceException;
	
	/**
	 * 删除退仓验收任务
	 * @param taskList
	 * @throws ServiceException
	 */
	public void deleteUmCheckTask(List<BillUmCheckTask> taskList) throws ServiceException;

	/**
	 * 退仓验收任务合计
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
    public Map<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams)throws ServiceException;
	
}