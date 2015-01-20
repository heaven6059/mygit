package com.yougou.logistics.city.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmInstockDtlMapper;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-17 20:35:58
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
@Service("billUmInstockDtlService")
class BillUmInstockDtlServiceImpl extends BaseCrudServiceImpl implements BillUmInstockDtlService {
	@Resource
	private BillUmInstockDtlMapper billUmInstockDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billUmInstockDtlMapper;
	}

	@Override
	public long findMaxInstockId(Map<String, Object> params) throws ServiceException {
		try {
			return billUmInstockDtlMapper.selectMaxInstockId(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) {
		return billUmInstockDtlMapper.selectSumQty(params, authorityParams);
	}

	@Override
	public int planSave(Map<String, Object> params) throws ServiceException {
		try {
			return billUmInstockDtlMapper.planSave(params);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

}