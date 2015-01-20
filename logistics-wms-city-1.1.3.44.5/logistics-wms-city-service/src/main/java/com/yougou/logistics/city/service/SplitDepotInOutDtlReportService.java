package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-16 下午2:08:46
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface SplitDepotInOutDtlReportService extends BaseCrudService {
	
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException;
	
}	
