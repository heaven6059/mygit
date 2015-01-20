package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.StoreSimple;

public interface StoreMapper extends BaseCrudMapper {

	/**
	 * 通过客户名称查找对应客户编码
	 * @param storeName
	 * @return
	 * @throws DaoException
	 */
	public Store queryStoreNo(String storeName) throws DaoException;

	public List<Store> selectByStoreName(@Param("params") Map<String, Object> params) throws DaoException;

	public String queryStoreByStoreNo(@Param("params") Store store) throws DaoException;

	public List<Store> queryStoreList(@Param("params") Store store, @Param("page") SimplePage page) throws DaoException;

	public int queryStoreCount(@Param("params") Store store) throws DaoException;

	public List<StoreSimple> selectAll4Simple(@Param("params") Map<String, Object> params);

	public List<Store> selectCircle();
	
	public List<Store> selectByParamsByBrand(@Param("params") Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<Store> selectWarehouseListByLocno(@Param("params") Map<String, Object> params)throws DaoException;
	
}