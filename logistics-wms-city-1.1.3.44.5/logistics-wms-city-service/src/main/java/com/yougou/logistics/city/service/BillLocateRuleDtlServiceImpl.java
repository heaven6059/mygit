package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.dal.database.BillLocateRuleDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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
@Service("billLocateRuleDtlService")
class BillLocateRuleDtlServiceImpl extends BaseCrudServiceImpl implements BillLocateRuleDtlService {
    @Resource
    private BillLocateRuleDtlMapper billLocateRuleDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billLocateRuleDtlMapper;
    }

	@Override
	public int findCategoryFilterCount(Map<String, Object> params, List<Category> listCategorys)
			throws ServiceException {
		try {
			return billLocateRuleDtlMapper.selectCategoryFilterCount(params, listCategorys);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	public List<Category> findCategoryFilterByPage(SimplePage page,Map<String, Object> params, List<Category> listCategorys)
			throws ServiceException {
		try {
			return billLocateRuleDtlMapper.selectCategoryFilterByPage(page,params, listCategorys);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillLocateRuleDtl> findBillLocateRuleDtlFilter(Map<String, Object> params,
			List<BillLocateRuleDtl> listLocateRuleDtls) throws ServiceException {
		try {
			return billLocateRuleDtlMapper.selectBillLocateRuleDtlFilter(params, listLocateRuleDtls);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

    
}