package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmCheckLatePrint;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-8-14 下午3:08:05
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillUmCheckLatePrintService extends BaseCrudService {

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;

	public List<BillUmCheckLatePrint> findItemInfoByBarcode(Map<String, Object> params, List<String> list)
			throws ServiceException;
}
