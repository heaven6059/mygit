package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.baseinfo.BrandDTO;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.service.BrandService;

@Service("brandManager")
class BrandManagerImpl  extends BaseCrudManagerImpl  implements BrandManager {
    @Resource
    private BrandService brandService;
    
    @Override
    public BaseCrudService init() {
        return brandService;
    }
    @Override
	public List<BrandDTO> queryBrandListByParentNo(String id,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return brandService.queryBrandListByParentNo(id,authorityParams);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}
	@Override
	public List<BrandDTO> findBrand(AuthorityParams authorityParams) throws ManagerException {
		try {
			return brandService.findBrand(authorityParams);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}
	@Override
	public List<Brand> findMyselfByParams(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return brandService.findMyselfByParams(params, authorityParams);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}
	
	
	
    
}