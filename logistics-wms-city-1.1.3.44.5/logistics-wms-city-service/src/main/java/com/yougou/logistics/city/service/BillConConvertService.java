package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
public interface BillConConvertService extends BaseCrudService {
	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
}