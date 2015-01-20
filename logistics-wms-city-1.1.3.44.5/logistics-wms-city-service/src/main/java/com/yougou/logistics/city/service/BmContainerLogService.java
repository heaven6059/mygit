package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmContainerLog;
/**
 * 容器操作日志
 * @author wanghb
 * @date   2014-8-16
 * @version 1.1.3.39
 */
public interface BmContainerLogService extends BaseCrudService {
	
	 Integer findDtlCount(Map<String, Object> params) throws ServiceException;
	 
	 List<BmContainerLog> findDtlByPage(SimplePage page,String sortColumn,String sortOrder,Map<String, Object> params)throws ServiceException;
}