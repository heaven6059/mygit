package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.baseinfo.StoreDTO;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.StoreSimple;

public interface StoreManager extends BaseCrudManager {

	List<StoreDTO> queryStoreListByParentNo(String parentNo) throws ManagerException;

	Store queryStoreNo(String storeName) throws ManagerException;

	List<Store> selectByStoreName(Map<String, Object> params) throws ManagerException;

	public String queryStoreByStoreNo(Store store) throws ManagerException;

	public List<Store> queryStoreList(Store store, SimplePage page) throws ManagerException;

	public int queryStoreCount(Store store) throws ManagerException;

	public List<StoreSimple> selectAll4Simple(Map<String, Object> params);
	
	public List<Store> queryCircle() throws ManagerException;
	
	public List<Store> findByParamsByBrand(Map<String, Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	public List<Store> selectWarehouseListByLocno(Map<String, Object> params) throws ManagerException;
}