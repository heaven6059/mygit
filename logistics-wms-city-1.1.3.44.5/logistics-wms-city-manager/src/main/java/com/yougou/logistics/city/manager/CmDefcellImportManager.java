package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpCmDefCellExcel;

/**
 * 
 * 储位manager
 * 
 * @author qin.dy
 * @date 2013-9-26 下午5:22:46
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefcellImportManager extends BaseCrudManager {
	/**
	 * 批量保存临时表
	 * @param list
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	Map<String,Object> importConBoxExcel(List<TmpCmDefCellExcel> list,SystemUser user)throws ManagerException;
	/**
	 * 删除本次导入
	 * @param uuId
	 * @return
	 * @throws ManagerException
	 */
	int deleteByUuid(String uuId)throws ManagerException;
	/**
	 * 批量保存数据
	 * @param params
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	Map<String, Object> batchSaveExcel(Map<String,Object> params,SystemUser user)throws ManagerException;
	
}