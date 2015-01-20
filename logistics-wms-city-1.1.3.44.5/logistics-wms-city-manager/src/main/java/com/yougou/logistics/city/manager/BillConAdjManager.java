package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
public interface BillConAdjManager extends BaseCrudManager {
	
	public int deleteStockAdj(String ids,String editor) throws ManagerException;

	/**
	 * 库存调整单合计
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public Map<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException;
	
}