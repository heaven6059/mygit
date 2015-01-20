package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.dal.database.ItemMapper;

@Service("itemService")
class ItemServiceImpl extends BaseCrudServiceImpl implements ItemService {
	@Resource
	private ItemMapper itemMapper;

	@Override
	public BaseCrudMapper init() {
		return itemMapper;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Item selectByCode(String cateCode, AuthorityParams authorityParams) throws ServiceException {
		Item item = null;
		try {
			item = itemMapper.selectByCode(cateCode,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return item;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int countItemAndSize(Item cc, AuthorityParams authorityParams) throws ServiceException {
		try {
			return itemMapper.countItemAndSize(cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<Item> findItemAndSizePage(SimplePage page, Item cc, AuthorityParams authorityParams) throws ServiceException {
		try {
			return itemMapper.findItemAndSizePage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findChPlanItemAndSizeCount(Item cc, AuthorityParams authorityParams) throws ServiceException {
		try {
			return itemMapper.selectChPlanItemAndSizeCount(cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<Item> findChPlanItemAndSizeByPage(SimplePage page, Item cc, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return itemMapper.selectChPlanItemAndSizeByPage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Item> findChPlanItemAndSize4Brand(Item cc)
			throws ServiceException {
		try {
			return itemMapper.selectChPlanItemAndSize4Brand(cc);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	@Override
	public List<Item> isChPlanItemCheck(Item item) throws ServiceException {
		try {
//			int count=itemMapper.selectChPlanItemCheck(item);
//			if(count>0){
//				return false;
//			}else{
//				return true;
//			}
			return itemMapper.selectChPlanItemCheck(item);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Item> queryBrandListByParentNo(String parentNo, AuthorityParams authorityParams)
			throws ServiceException {
		if (StringUtils.isBlank(parentNo)) {
			parentNo = "0";
		}
		List<Item> resultList = new ArrayList<Item>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brandNoHead", parentNo);
		 resultList = itemMapper.selectByParams2( params,authorityParams);
		
		return resultList;
	}
	
	

	
}