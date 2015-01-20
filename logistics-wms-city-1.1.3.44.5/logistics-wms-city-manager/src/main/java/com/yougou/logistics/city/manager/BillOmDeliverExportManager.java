package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmDeliverExport;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 
 * 
 * @author jiang.ys
 * @date 2013-10-12 下午3:26:07
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverExportManager extends BaseCrudManager {
	public List<Map<String, Object>> findDeliverDtlSize(Map<String,Object> params)throws ManagerException;
	public List<Map<String, Object>> findDeliverDtlSizeNum(Map<String,Object> params, AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 获取size_kind
	 * @param model
	 * @param authorityParams
	 * @return
	 */
	public List<String> findAllDtlSizeKind(Map<String, Object> params, AuthorityParams authorityParams);
	
	public Map<String,Object> findBillOmDeliverExportByPage(BillOmDeliverExport rep, AuthorityParams authorityParams, boolean all)throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
}