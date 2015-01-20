package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.baseinfo.StoreDTO;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.StoreSimple;
import com.yougou.logistics.city.service.StoreService;

@Service("storeManager")
class StoreManagerImpl extends BaseCrudManagerImpl implements StoreManager {
	@Resource
	private StoreService storeService;

	@Override
	public BaseCrudService init() {
		return storeService;
	}

	@Override
	public List<StoreDTO> queryStoreListByParentNo(String parentNo) throws ManagerException {
		try {
			return storeService.queryStoreListByParentNo(parentNo);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

	@Override
	public Store queryStoreNo(String storeName) throws ManagerException {
		try {
			return storeService.queryStoreNo(storeName);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

	@Override
	public List<Store> selectByStoreName(Map<String, Object> params) throws ManagerException {
		try {
			return storeService.selectByStoreName(params);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

	public String queryStoreByStoreNo(Store store) throws ManagerException {
		try {
			return storeService.queryStoreByStoreNo(store);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

	@Override
	public List<Store> queryStoreList(Store store, SimplePage page) throws ManagerException {
		try {
			return storeService.queryStoreList(store, page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();

		}
	}

	@Override
	public int queryStoreCount(Store store) throws ManagerException {
		try {
			return storeService.queryStoreCount(store);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	public List<StoreSimple> selectAll4Simple(Map<String, Object> params) {
		return storeService.selectAll4Simple(params);
	}

	@Override
	public List<Store> queryCircle() throws ManagerException {
		return storeService.queryCircle();
	}

	@Override
	public List<Store> findByParamsByBrand(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return storeService.findByParamsByBrand(params, authorityParams);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public List<Store> selectWarehouseListByLocno(Map<String, Object> params)
			throws ManagerException {
		try {
			return storeService.selectWarehouseListByLocno(params);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

}