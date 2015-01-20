package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface ItemCellContentService extends BaseCrudService {

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,String[] areaNos,AuthorityParams authorityParams) throws ServiceException;
	
}