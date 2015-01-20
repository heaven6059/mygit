package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteKey;
import com.yougou.logistics.city.dal.mapper.BillSmWasteMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
@Service("billSmWasteService")
class BillSmWasteServiceImpl extends BaseCrudServiceImpl implements BillSmWasteService {
    @Resource
    private BillSmWasteMapper billSmWasteMapper;

    @Override
    public BaseCrudMapper init() {
        return billSmWasteMapper;
    }

	@Override
	public int deleteDtlById(BillSmWasteKey key) throws ServiceException {
		try {
			return billSmWasteMapper.deleteDtlById(key);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmWaste> findByWaste(BillSmWaste modelType,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmWasteMapper.selectByWaste(modelType, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billSmWasteMapper.selectSumQty(params, authorityParams);
	}
}