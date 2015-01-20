package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.CategorySimpleDto;
import com.yougou.logistics.city.common.dto.baseinfo.CategoryDTO;
import com.yougou.logistics.city.common.model.Category;

public interface CategoryService extends BaseCrudService {

	List<CategoryDTO> queryCategoryListByParentNo(String parentNo, String sysNo) throws ServiceException;

	public abstract Category selectByCateCode(String cateCode) throws ServiceException;
	
	public List<CategorySimpleDto> findCategory4Simple(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
}