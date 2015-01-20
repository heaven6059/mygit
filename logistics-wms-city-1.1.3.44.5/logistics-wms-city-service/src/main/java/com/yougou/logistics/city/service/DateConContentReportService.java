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
 * @date 2014-2-22 下午6:59:51
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface DateConContentReportService extends BaseCrudService {
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException;
}
