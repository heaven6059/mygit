package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.baseinfo.BrandDTO;
import com.yougou.logistics.city.common.model.Brand;

public interface BrandService  extends BaseCrudService {

	public List<BrandDTO> queryBrandListByParentNo(String parentNo ,AuthorityParams authorityParams) throws ServiceException;
	
	public abstract Brand selectByCateCode(String cateCode,AuthorityParams authorityParams) throws ServiceException;
	//public  Brand findById(String cateCode,AuthorityParams authorityParams) throws ServiceException;

	public List<BrandDTO> findBrand(AuthorityParams authorityParams)throws ServiceException;
	public List<Brand> findMyselfByParams(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
}