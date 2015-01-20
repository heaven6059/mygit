package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BmContainerLog;

/**
 * 容器操作日志
 * @author wanghb
 * @date   2014-8-16
 * @version 1.1.3.39
 */
@Service("bmContainerLogManager")
public interface BmContainerLogManager extends BaseCrudManager {
	
	Integer findDtlCount(Map<String, Object> params )throws ManagerException ;
	
	List<BmContainerLog> findDtlByPage(SimplePage page,String sortColumn,String sortOrder,Map<String, Object> params)throws ManagerException;
}