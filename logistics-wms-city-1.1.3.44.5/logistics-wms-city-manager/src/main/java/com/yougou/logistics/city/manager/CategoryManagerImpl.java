package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.CategorySimpleDto;
import com.yougou.logistics.city.common.dto.baseinfo.CategoryDTO;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.service.CategoryService;

@Service("categoryManager")
class CategoryManagerImpl extends BaseCrudManagerImpl implements CategoryManager {
	
    @Resource
    private CategoryService categoryService;

    @Override
    public BaseCrudService init() {
        return categoryService;
    }

	@Override
	public List<CategoryDTO> queryCategoryListByParentNo(String parentNo,String sysNo)
			throws ManagerException {
		try {
			return categoryService.queryCategoryListByParentNo(parentNo,sysNo);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

	@Override
	public List<CategorySimpleDto> findCategory4Simple(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return categoryService.findCategory4Simple(map,authorityParams);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

}