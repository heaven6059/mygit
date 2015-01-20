package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillContainerTask;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
public interface BillContainerTaskService extends BaseCrudService {

	/**
	 * 批量添加容器任务记录表
	 * @param containerTask
	 * @param taskDtlList
	 * @return
	 * @throws ServiceException
	 */
	public int insertBillContainerTask(BillContainerTask containerTask, List<BillContainerTaskDtl> taskDtlList)
			throws ServiceException;
}