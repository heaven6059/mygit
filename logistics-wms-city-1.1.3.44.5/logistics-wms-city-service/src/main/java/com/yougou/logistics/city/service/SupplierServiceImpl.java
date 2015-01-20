package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.SupplierSimple;
import com.yougou.logistics.city.dal.database.SupplierMapper;

@Service("supplierService")
class SupplierServiceImpl extends BaseCrudServiceImpl implements SupplierService {
	@Resource
	private SupplierMapper supplierMapper;

	@Override
	public BaseCrudMapper init() {
		return supplierMapper;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<SupplierSimple> selectAll4Simple(Map<String, Object> map, AuthorityParams authorityParams) {
		return supplierMapper.selectAll4Simple(map, authorityParams);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<SupplierSimple> findSupplierByRecede(Map<String, Object> map,
			AuthorityParams authorityParams) {
		return supplierMapper.selectSupplierByRecede(map, authorityParams);
	}

}