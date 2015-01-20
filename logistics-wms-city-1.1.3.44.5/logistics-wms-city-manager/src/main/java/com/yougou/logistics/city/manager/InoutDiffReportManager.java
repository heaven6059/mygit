package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-6-23 上午10:28:48
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface InoutDiffReportManager extends BaseCrudManager {
	
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException;

}
