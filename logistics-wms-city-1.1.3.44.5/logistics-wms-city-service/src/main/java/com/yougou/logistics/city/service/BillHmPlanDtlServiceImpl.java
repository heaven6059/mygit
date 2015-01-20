package com.yougou.logistics.city.service;

import java.util.List;
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
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillHmPlanDtlMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
@Service("billHmPlanDtlService")
class BillHmPlanDtlServiceImpl extends BaseCrudServiceImpl implements BillHmPlanDtlService {

	@Resource
	private BillHmPlanDtlMapper billHmPlanDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billHmPlanDtlMapper;
	}

	@Override
	public int selectMaxPid(BillHmPlanDtl billHmPlanDtl) throws ServiceException {
		try {
			return billHmPlanDtlMapper.selectMaxPid(billHmPlanDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	@Override
	public int addByWmPlan(List<BillHmPlanDtl> addPlanDtlList) throws ServiceException {
		try {
			return billHmPlanDtlMapper.insertByWmPlan(addPlanDtlList);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	public List<BillHmPlanDtl> findDuplicateRecord(Map<String, Object> params)
			throws ServiceException {
		try{
			return billHmPlanDtlMapper.selectDuplicateRecord(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
		
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billHmPlanDtlMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillHmPlanDtl> findInsertHmPlan4WmPlan(Map<String, Object> params) throws ServiceException {
		try {
			return billHmPlanDtlMapper.selectInsertHmPlan4WmPlan(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillHmPlanDtl> selectBillHmPlanDtl(Map<String, Object> params, AuthorityParams authorityParams) 
			throws ServiceException {
		try {
			return billHmPlanDtlMapper.selectBillHmPlanDtl(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}