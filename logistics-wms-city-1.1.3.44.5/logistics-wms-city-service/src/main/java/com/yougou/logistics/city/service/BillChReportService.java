package com.yougou.logistics.city.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;

public interface BillChReportService extends BaseCrudService {
	
	/**
	 * 总计
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
    public Map<String, Integer> findSumQty(@Param("params")Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;
}