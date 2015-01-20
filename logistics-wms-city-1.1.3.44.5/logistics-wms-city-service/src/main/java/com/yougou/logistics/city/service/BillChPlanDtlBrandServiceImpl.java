package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BillChPlanDtlBrandMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

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
@Service("billChPlanDtlBrandService")
class BillChPlanDtlBrandServiceImpl extends BaseCrudServiceImpl implements
		BillChPlanDtlBrandService {
	@Resource
	private BillChPlanDtlBrandMapper billChPlanDtlBrandMapper;

	@Override
	public BaseCrudMapper init() {
		return billChPlanDtlBrandMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public Integer batchInsertPlanDtlBrand(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChPlanDtlBrandMapper.batchInsertPlanDtlBrand(params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

}