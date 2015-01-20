package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.CmDefarea;

/**
 * 
 * 库区manager
 * 
 * @author qin.dy
 * @date 2013-9-26 上午10:07:25
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefareaManager extends BaseCrudManager {

	public Map<String, Object> deleteBatch(String locno,List<CmDefarea> listCmDefareas)throws ManagerException;

	public List<CmDefarea> findByStoreroom(Map<String, Object> params);
}