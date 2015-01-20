package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.vo.AccInventoryConVo;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-15 17:22:25
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
public interface AccInventoryConService extends BaseCrudService {
	
	/**
     * 记录容器库存
     * @param accInventoryConVo
     */
	public void accontingForCon(AccInventoryConVo accInventoryConVo) throws ServiceException;
	
	/**
     * 记录容器库存(事务同步)
     * @param accInventoryConVo
     */
	public void accontingForConByTran(AccInventoryConVo accInventoryConVo) throws ServiceException;
	
}