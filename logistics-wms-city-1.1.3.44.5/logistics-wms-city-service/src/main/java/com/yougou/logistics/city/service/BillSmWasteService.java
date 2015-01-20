package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteKey;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillSmWasteService extends BaseCrudService {
	
	public int deleteDtlById(BillSmWasteKey key) throws ServiceException;
	
	public List<BillSmWaste> findByWaste(BillSmWaste billSmWaste,Map<String,Object> params, AuthorityParams authorityParams)throws ServiceException;
	
	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
}