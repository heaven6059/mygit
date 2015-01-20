package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-12-24 下午2:14:56
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface ConItemInfoService extends BaseCrudService {

	public String findMaxItemIdByItemNo(String itemNo) throws ServiceException;
}
