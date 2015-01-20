package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SupplierSimple;

public interface SupplierManager extends BaseCrudManager {
	public List<SupplierSimple> selectAll4Simple(Map<String, Object> map, AuthorityParams authorityParams);
	/**
	 * 退厂确认加载供应商
	 * @param map
	 * @param authorityParams
	 * @return
	 */
	public List<SupplierSimple> findSupplierByRecede(Map<String, Object> map, AuthorityParams authorityParams);
}