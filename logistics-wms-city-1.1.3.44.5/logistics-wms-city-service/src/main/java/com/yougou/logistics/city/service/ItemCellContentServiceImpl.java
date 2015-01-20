package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.ItemCellContentMapper;

@Service("itemCellContentService")
class ItemCellContentServiceImpl extends BaseCrudServiceImpl implements
		ItemCellContentService {
	@Resource
	private ItemCellContentMapper itemCellContentMapper;

	@Override
	public BaseCrudMapper init() {
		return itemCellContentMapper;
	}

	@Override
	public int findCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		String areaNo = String.valueOf(params.get("areaNo"));
		List<String> areaNoList = new ArrayList<String>();
		if (!StringUtils.isEmpty(areaNo)) {
			String[] areaNos = areaNo.split(",");
			areaNoList = Arrays.asList(areaNos);
		}
		return this.itemCellContentMapper.selectCount(params,authorityParams, areaNoList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <ModelType> List<ModelType> findByPage(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		String areaNo = String.valueOf(params.get("areaNo"));
		List<String> areaNoList = new ArrayList<String>();
		if (!StringUtils.isEmpty(areaNo)) {
			String[] areaNos = areaNo.split(",");
			areaNoList = Arrays.asList(areaNos);
		}
		return (List<ModelType>) this.itemCellContentMapper.selectByPage(page,
				orderByField, orderBy, params, authorityParams, areaNoList);
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,String[] areaNos,AuthorityParams authorityParams) throws ServiceException {
		try {
			return itemCellContentMapper.selectSumQty(map,authorityParams,areaNos);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

}