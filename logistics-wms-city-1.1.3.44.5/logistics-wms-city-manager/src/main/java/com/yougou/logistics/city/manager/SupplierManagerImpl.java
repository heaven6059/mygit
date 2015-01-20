package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.SupplierSimple;
import com.yougou.logistics.city.service.SupplierService;

@Service("supplierManager")
class SupplierManagerImpl extends BaseCrudManagerImpl implements SupplierManager {
	@Resource
	private SupplierService supplierService;

	@Override
	public BaseCrudService init() {
		return supplierService;
	}

	public List<SupplierSimple> selectAll4Simple(Map<String, Object> map, AuthorityParams authorityParams) {
		return supplierService.selectAll4Simple(map, authorityParams);
	}

	@Override
	public List<SupplierSimple> findSupplierByRecede(Map<String, Object> map,
			AuthorityParams authorityParams) {
		return supplierService.findSupplierByRecede(map, authorityParams);
	}
}
