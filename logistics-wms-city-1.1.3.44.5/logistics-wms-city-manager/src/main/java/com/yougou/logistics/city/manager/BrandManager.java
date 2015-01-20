package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.baseinfo.BrandDTO;
import com.yougou.logistics.city.common.model.Brand;

public interface BrandManager  extends BaseCrudManager {

	public List<BrandDTO> queryBrandListByParentNo(String id,AuthorityParams authorityParams) throws ManagerException;

	public List<BrandDTO> findBrand(AuthorityParams authorityParams)throws ManagerException;
	public List<Brand> findMyselfByParams(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	
}