package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillImCheck;

/**
 * 
 * 收货验收单service
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:15:37
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillImCheckService extends BaseCrudService {

	public void check(List<BillImCheck> checkList, String userId,String username) throws ServiceException;

	public int findCheckForDirectCount(Map map, AuthorityParams authorityParams) throws ServiceException;

	public List<BillImCheck> selectByPageForDirect(SimplePage page, Map map, AuthorityParams authorityParams) throws ServiceException;
	
	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
}