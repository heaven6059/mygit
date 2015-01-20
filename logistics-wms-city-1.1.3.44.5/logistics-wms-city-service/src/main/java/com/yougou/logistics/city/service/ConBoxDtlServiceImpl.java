package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.ConBoxDtlMapper;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 21:07:33
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
@Service("conBoxDtlService")
class ConBoxDtlServiceImpl extends BaseCrudServiceImpl implements ConBoxDtlService {
	@Resource
	private ConBoxDtlMapper conBoxDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return conBoxDtlMapper;
	}

	@Override
	public List<ConBoxDtl> findByParam(Map<String, Object> queryParam) throws ServiceException {
		try {
			return conBoxDtlMapper.findByParam(queryParam);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int countBoxAndNum(ConBoxDtl cc, AuthorityParams authorityParams) throws ServiceException {
		try {
			return conBoxDtlMapper.countItemAndSize(cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<ConBoxDtl> findCnBoxAndNumPage(SimplePage page, ConBoxDtl cc, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return conBoxDtlMapper.findCnBoxAndNumPage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectItem4umuntreadCount(Map<String, String> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return conBoxDtlMapper.selectItem4umuntreadCount(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<ConBoxDtl> selectItem4umuntread(Map<String, String> map, SimplePage page,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return conBoxDtlMapper.selectItem4umuntread(map, page, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ConBoxDtl selectBoxDtl4Recheck(Map<String, Object> params) throws ServiceException {
		try {
			return conBoxDtlMapper.selectBoxDtl4Recheck(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer selectMaxBoxId(ConBoxDtl boxDtl) throws ServiceException {
		try {
			return conBoxDtlMapper.selectMaxBoxId(boxDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	@Override
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException{
		try {
			return conBoxDtlMapper.selectSumQty(map,authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}