package com.yougou.logistics.city.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillWmRequestDtlMapper;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 17:59:52 CST 2014
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
@Service("billWmRequestDtlService")
class BillWmRequestDtlServiceImpl extends BaseCrudServiceImpl implements BillWmRequestDtlService {
	@Resource
	private BillWmRequestDtlMapper billWmRequestDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billWmRequestDtlMapper;
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billWmRequestDtlMapper.selectSumQty(map, authorityParams);
	}

	@Override
	public int addByWmPlan(Map<String, Object> params) throws ServiceException {
		try {
			return billWmRequestDtlMapper.insertByWmPlan(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
}