package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 请写出类的用途
 * 
 * @author su.yq
 * @date 2014-11-12 14:17:19
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public interface BillChPlanDtlBrandService extends BaseCrudService {

	/**
	 * 批量插入品牌
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public Integer batchInsertPlanDtlBrand(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;
}