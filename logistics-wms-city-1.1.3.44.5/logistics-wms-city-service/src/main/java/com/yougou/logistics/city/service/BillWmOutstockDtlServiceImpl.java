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
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillWmOutstockDtlMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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
@Service("billWmOutstockDtlService")
class BillWmOutstockDtlServiceImpl extends BaseCrudServiceImpl implements BillWmOutstockDtlService {
    
	@Resource
    private BillWmOutstockDtlMapper billWmOutstockDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billWmOutstockDtlMapper;
    }

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmOutstockDtlDto> findOutstockDtlItem(BillWmOutstockDtl billWmOutstockDtl, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmOutstockDtlMapper.selectOutstockDtlItem(billWmOutstockDtl, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmOutstockDtlMapper.selectSumQty(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmOutstockDtlDto> findOutstockDtlItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmOutstockDtlMapper.selectOutstockDtlItemByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findOutstockDtlItemCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billWmOutstockDtlMapper.selectOutstockDtlItemCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectOutstockDtlItemSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmOutstockDtlMapper.selectOutstockDtlItemSumQty(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}