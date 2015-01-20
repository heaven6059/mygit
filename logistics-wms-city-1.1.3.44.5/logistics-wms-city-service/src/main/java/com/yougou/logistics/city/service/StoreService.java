package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.baseinfo.StoreDTO;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.StoreSimple;

public interface StoreService extends BaseCrudService {

	List<StoreDTO> queryStoreListByParentNo(String parentNo) throws ServiceException;

	Store queryStoreNo(String storeName) throws ServiceException;

	List<Store> selectByStoreName(Map<String, Object> params) throws ServiceException;

	public String queryStoreByStoreNo(Store store) throws ServiceException;

	public List<Store> queryStoreList(Store store, SimplePage page) throws ServiceException;

	public int queryStoreCount(Store store) throws ServiceException;

	public List<StoreSimple> selectAll4Simple(Map<String, Object> params);
	
	public List<Store> queryCircle();
	
	public List<Store> findByParamsByBrand(Map<String, Object> params, AuthorityParams authorityParams)throws ServiceException;
	
	public List<Store> selectWarehouseListByLocno(Map<String, Object> params)throws ServiceException;
}