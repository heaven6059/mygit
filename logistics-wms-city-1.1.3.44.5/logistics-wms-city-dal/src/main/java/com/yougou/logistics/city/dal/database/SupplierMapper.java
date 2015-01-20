package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.SupplierSimple;

public interface SupplierMapper extends BaseCrudMapper {

	public List<SupplierSimple> selectAll4Simple(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 退厂确认加载供应商
	 * @param map
	 * @param authorityParams
	 * @return
	 */
	public List<SupplierSimple> selectSupplierByRecede(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams);
}