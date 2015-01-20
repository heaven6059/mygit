package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDeliverExport;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 
 * 
 * @author jiang.ys
 * @date 2013-10-12 下午3:27:00
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverExportService extends BaseCrudService {
	
	public List<Map<String, Object>> findDeliverDtlSize(Map<String,Object> params) throws ServiceException;
	public List<Map<String, Object>> findDeliverDtlSizeNum(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException;
	public List<String> findAllDtlSizeKind(Map<String, Object> params, AuthorityParams authorityParams);
	public Map<String,Object> findBillOmDeliverExportByPage(BillOmDeliverExport rep, AuthorityParams authorityParams, boolean all)throws ServiceException;
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams)throws ServiceException;
}