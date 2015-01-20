package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
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
import com.yougou.logistics.city.common.dto.AuthorityUserOrganizationDto;
import com.yougou.logistics.city.common.dto.baseinfo.StoreDTO;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.StoreSimple;
import com.yougou.logistics.city.dal.database.StoreMapper;
import com.yougou.logistics.city.dal.database.UserOrganizationMapper;

@Service("storeService")
class StoreServiceImpl extends BaseCrudServiceImpl implements StoreService {
	@Resource
	private StoreMapper storeMapper;
	
	@Resource
	private UserOrganizationMapper userOrganizationMapper;

	@Override
	public BaseCrudMapper init() {
		return storeMapper;
	}

	@Override
	public List<StoreDTO> queryStoreListByParentNo(String parentNo) throws ServiceException {
		if (StringUtils.isBlank(parentNo)) {
			parentNo = "0";
		}
		List<StoreDTO> resultList = new ArrayList<StoreDTO>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("storeNoHead", parentNo);
		params.put("isTree", '1');
		List<Store> tempList = storeMapper.selectByParams(null, params);
		if (tempList != null && tempList.size() > 0) {
			for (Store store : tempList) {
				StoreDTO storeDTO = new StoreDTO();
				try {
					BeanUtils.copyProperties(storeDTO, store);
				} catch (Exception e) {
					throw new ServiceException(e);
				}
				if (!("0").equals(store.getStoreNoHead()) && StringUtils.isNotBlank(store.getStoreNo())) {
					storeDTO.setStoreName(storeDTO.getStoreName() + "(" + store.getStoreNo() + ")");
				}
				int childrenCount = store.getChildrenCount();
				if (childrenCount > 0) {
					storeDTO.setState("closed");
				} else {
					storeDTO.setState("open");
				}
				resultList.add(storeDTO);
			}
		}
		return resultList;
	}

	@Override
	public Store queryStoreNo(String storeName) throws ServiceException {
		Store store = null;
		try {
			if (storeName != null && storeName.length() > 0) {
				store = storeMapper.queryStoreNo(storeName);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return store;
	}

	@Override
	public List<Store> selectByStoreName(Map<String, Object> params) throws ServiceException {
		try {
			return storeMapper.selectByStoreName(params);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public String queryStoreByStoreNo(Store store) throws ServiceException {
		try {
			return storeMapper.queryStoreByStoreNo(store);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Store> queryStoreList(Store store, SimplePage page) throws ServiceException {
		try {
			return storeMapper.queryStoreList(store, page);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	@Override
	public int queryStoreCount(Store store) throws ServiceException {
		try {
			return storeMapper.queryStoreCount(store);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public List<StoreSimple> selectAll4Simple(Map<String, Object> params) {
		return storeMapper.selectAll4Simple(params);
	}

	@Override
	public List<Store> queryCircle() {
		return storeMapper.selectCircle();
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<Store> findByParamsByBrand(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return storeMapper.selectByParamsByBrand(params, authorityParams);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Store> selectWarehouseListByLocno(Map<String, Object> params) throws ServiceException {
		try {
			List<AuthorityUserOrganizationDto>	userOrganizationDtolist =userOrganizationMapper.selectUserOrganizationByParams(params);
			List<Store> list = storeMapper.selectWarehouseListByLocno(params);
			for (int i = 0; i < list.size(); i++) {
				if(params.get("locno").toString().equals(list.get(i).getStoreNo())){
					list.remove(i);
					i--;
					continue;
				}
				for (int j = 0; j < userOrganizationDtolist.size(); j++) {
					if(userOrganizationDtolist.get(j).getOrganizationNo().toString().equals(list.get(i).getStoreNo())){
						list.remove(i);
						i--;
					}
				}
			}
			return list;
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
}