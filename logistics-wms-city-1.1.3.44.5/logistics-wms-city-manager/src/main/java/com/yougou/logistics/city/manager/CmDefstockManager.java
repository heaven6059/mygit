package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.CmDefstock;

/**
 * 
 * 通道manager
 * 
 * @author qin.dy
 * @date 2013-9-26 下午4:01:49
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefstockManager extends BaseCrudManager {
	
	public  int addCascade(CmDefstock cmDefstock) throws ManagerException ;

	public Map<String, Object> deleteBatch(String locno,List<CmDefstock> listCmDefstocks)throws ManagerException ;

	public int queryStoreNo0(CmDefstock cmDefstock) throws ManagerException;
	
	public int queryStoreNo1(CmDefstock cmDefstock) throws ManagerException;
	
	public int queryStoreNo2(CmDefstock cmDefstock) throws ManagerException;
}