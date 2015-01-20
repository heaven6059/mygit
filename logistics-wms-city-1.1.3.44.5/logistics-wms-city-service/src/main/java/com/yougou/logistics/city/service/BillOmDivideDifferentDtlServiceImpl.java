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
import com.yougou.logistics.city.common.model.BillOmDivideDifferentDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillOmDivideDifferentDtlMapper;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
@Service("billOmDivideDifferentDtlService")
class BillOmDivideDifferentDtlServiceImpl extends BaseCrudServiceImpl implements BillOmDivideDifferentDtlService {
	
	@Resource
	private BillOmDivideDifferentDtlMapper billOmDivideDifferentDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billOmDivideDifferentDtlMapper;
	}

	@Override
	public List<BillOmDivideDifferentDtl> findDifferentDtlGroupByItem(Map<String, Object> params)
			throws ServiceException {
		try {
			return billOmDivideDifferentDtlMapper.selectDifferentDtlGroupByItem(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmDivideDifferentDtlMapper.selectSumQty(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public int selectMaxRowId(BillOmDivideDifferentDtl differentDtl) throws ServiceException {
		try {
			return billOmDivideDifferentDtlMapper.selectMaxRowId(differentDtl);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public BillOmDivideDifferentDtl selectDifferentByPixFlag(BillOmDivideDifferentDtl differentDtl)
			throws ServiceException {
		try {
			return billOmDivideDifferentDtlMapper.selectDifferentByPixFlag(differentDtl);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public List<BillOmDivideDifferentDtl> selectDifferentDtl4Content(Map<String, Object> params)
			throws ServiceException {
		try {
			return billOmDivideDifferentDtlMapper.selectDifferentDtl4Content(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

//	@Override
//	public BillOmDivideDifferentDtl selectDtlByDivideDtl(BillOmDivideDifferentDtl differentDtl) throws ServiceException {
//		try {
//			return billOmDivideDifferentDtlMapper.selectDtlByDivideDtl(differentDtl);
//		} catch (Exception e) {
//			throw new ServiceException("", e);
//		}
//	}
}