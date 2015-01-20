package com.yougou.logistics.city.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.ItemCollectContentMapper;

@Service("itemCollectContentService")
class ItemCollectContentServiceImpl extends BaseCrudServiceImpl implements ItemCollectContentService {
	@Resource
	private ItemCollectContentMapper itemCollectContentMapper;

	@Override
	public BaseCrudMapper init() {
		return itemCollectContentMapper;
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return itemCollectContentMapper.selectSumQty(map,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
}