package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmContainerLog;
import com.yougou.logistics.city.service.BmContainerLogService;

/**
 * 容器日志
 * @author wanghb
 * @date 2013-9-22 下午3:03:15
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("bmContainerLogManager")
class BmContainerLogManagerImpl extends BaseCrudManagerImpl implements BmContainerLogManager {
    @Resource
	private BmContainerLogService bmContainerLogService;
	
	@Override
	protected BaseCrudService init() {
		return bmContainerLogService;
	}
	public Integer findDtlCount(Map<String, Object> params )throws ManagerException{
		try {
			return bmContainerLogService.findDtlCount(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	public  List<BmContainerLog> findDtlByPage(SimplePage page,String sortColumn,String sortOrder,Map<String, Object> params)throws ManagerException{
		try {
			return bmContainerLogService.findDtlByPage(page, sortColumn, sortOrder, params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

}