package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.CmDefware;

/**
 * 仓区manager
 * 
 * @author qin.dy
 * @date 2013-9-25 下午3:39:21
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefwareManager extends BaseCrudManager {
	
	public Map<String, Object> deleteBatch(String locno,List<CmDefware> listDefareas) throws ManagerException;
}