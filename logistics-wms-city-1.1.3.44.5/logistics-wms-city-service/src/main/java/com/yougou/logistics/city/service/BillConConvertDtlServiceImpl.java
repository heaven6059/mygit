package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillConConvertDtl;
import com.yougou.logistics.city.common.model.BillConConvertDtlSizeDto;
import com.yougou.logistics.city.dal.database.BillConConvertDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
@Service("billConConvertDtlService")
class BillConConvertDtlServiceImpl extends BaseCrudServiceImpl implements BillConConvertDtlService {
    @Resource
    private BillConConvertDtlMapper billConConvertDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billConConvertDtlMapper;
    }

	@Override
	public int findContentCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billConConvertDtlMapper.selectContentCount(params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillConConvertDtl> findContentByPage(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billConConvertDtlMapper.selectContentByPage(page, orderByField, orderBy,params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public void batchInsertDtl(List<BillConConvertDtl> list)
			throws ServiceException {
		try{
			billConConvertDtlMapper.batchInsertDtl(list);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params) {
		return billConConvertDtlMapper.selectSumQty(params);
	}

	@Override
	public List<BillConConvertDtlSizeDto> findDtl4SizeHorizontal(
			Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billConConvertDtlMapper.selectDetail4SizeHorizontal(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException("",e);
		}
	}
}