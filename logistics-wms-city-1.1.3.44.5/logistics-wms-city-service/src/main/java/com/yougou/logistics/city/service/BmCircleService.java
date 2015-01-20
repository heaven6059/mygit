package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

public interface BmCircleService extends BaseCrudService {

	/**
	 * 删除商圈
	 * @param locnoStrs
	 * @return
	 * @throws ManagerException
	 */
	public int deleteCircle(String circleNo) throws ServiceException;
}