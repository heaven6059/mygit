package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SplitDepotDateSumReport;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-18 下午1:44:16
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface SplitDepotDateSumReportManager extends BaseCrudManager {
	
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	public List<SplitDepotDateSumReport> findSplitDepotDateSumReportList(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException;

}
