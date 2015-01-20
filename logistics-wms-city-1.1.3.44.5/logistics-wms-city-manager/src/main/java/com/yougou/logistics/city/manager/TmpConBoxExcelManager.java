package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;

/**
 * 导入箱明细初始化
 * @author wanghb
 * @date 2014-10-11 上午11:44:58
 * @version 1.1.3.41
 * @copyright yougou.com 
 */
public interface TmpConBoxExcelManager extends BaseCrudManager {
  
	Map<String,Object> importConBoxExcel(List<TmpConBoxExcel> list,SystemUser user)throws ManagerException;
	
	Map<String, Object> batchSaveConBoxExcel(Map<String,Object> params,SystemUser user)throws ManagerException;
	
	int deleteByUuid(String uuId)throws ManagerException;
}
