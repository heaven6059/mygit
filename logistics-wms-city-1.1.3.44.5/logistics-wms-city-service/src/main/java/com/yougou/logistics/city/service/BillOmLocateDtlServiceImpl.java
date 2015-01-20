package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.model.BillOmLocateDtlSizeKind;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillOmLocateDtlMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 13:58:52 CST 2013
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
@Service("billOmLocateDtlService")
class BillOmLocateDtlServiceImpl extends BaseCrudServiceImpl implements BillOmLocateDtlService {

	@Resource
	private BillOmLocateDtlMapper billOmLocateDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billOmLocateDtlMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmLocateDtlMapper.selectSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmLocateDtl> findBillOmLocateDtlGroupBy(Map<String, Object> params) throws ServiceException {
		try {
			return billOmLocateDtlMapper.selectBillOmLocateDtlGroupBy(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNoItemNoExpNo(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmLocateDtlMapper.selectDtlByStoreNoItemNoExpNo(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmLocateDtlSizeKind> selectAllDtl4Print(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmLocateDtlMapper.selectAllDtl4Print(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<String> selectAllDtlSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmLocateDtlMapper.selectAllDtlSizeKind(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNo(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmLocateDtlMapper.selectDtlByStoreNo(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}