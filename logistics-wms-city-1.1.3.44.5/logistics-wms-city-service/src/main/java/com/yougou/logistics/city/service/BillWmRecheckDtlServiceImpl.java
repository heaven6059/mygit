package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillWmRecheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillWmRecheckDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
@Service("billWmRecheckDtlService")
class BillWmRecheckDtlServiceImpl extends BaseCrudServiceImpl implements BillWmRecheckDtlService {
    @Resource
    private BillWmRecheckDtlMapper billWmRecheckDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billWmRecheckDtlMapper;
    }
    
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findWmRecheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billWmRecheckDtlMapper.selectWmRecheckDtlCount(params, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecheckDtl> findWmRecheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmRecheckDtlMapper.selectWmRecheckDtlByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findWmRecheckDtlGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billWmRecheckDtlMapper.selectWmRecheckDtlGroupByCount(params, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecheckDtl> findWmRecheckDtlGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmRecheckDtlMapper.selectWmRecheckDtlGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectWmRecheckDtlSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billWmRecheckDtlMapper.selectWmRecheckDtlSumQty(params, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	
//	@Override
//	public Long getItemIdByRecheckNo(BillWmRecheckDtl billWmRecheckDtl)
//			throws ServiceException {
//		try{
//			return  billWmRecheckDtlMapper.getItemIdByRecheckNo(billWmRecheckDtl);
//		}catch(Exception e){
//			throw new ServiceException(e);
//		}
//	}
}