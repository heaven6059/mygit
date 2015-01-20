package com.yougou.logistics.city.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 记账任务清单(异步记账)
 * @author wugy
 * @date  2014-07-23 18:31:36
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
public interface AccTaskService extends BaseCrudService {
	
	/**
	 * 处理记账清单
	 * @throws ServiceException
	 */
	public void executeAcctask() throws ServiceException;
	
	/**
	 * 记账业务通用sql查询
	 * @param params
	 * @throws ServiceException
	 */
	public void testqueryAccCheckDtlVoList(@Param("params") Map<String, Object> params)	throws ServiceException;
	
}