package com.yougou.logistics.city.service;

import java.util.Map;


import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-06-18 10:38:59
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
public interface ConContentHistoryService extends BaseCrudService {
	
	public Integer selectSumByLocno(Map<String, Object> map)throws ServiceException;
	
	public int insertByContent(Map<String, Object> map)throws ServiceException;
	
}