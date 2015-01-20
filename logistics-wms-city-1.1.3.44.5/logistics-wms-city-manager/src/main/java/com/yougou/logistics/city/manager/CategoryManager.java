package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.CategorySimpleDto;
import com.yougou.logistics.city.common.dto.baseinfo.CategoryDTO;

public interface CategoryManager extends BaseCrudManager {
	
	List<CategoryDTO> queryCategoryListByParentNo(String parentNo,String sysNo) throws ManagerException;
	
	/**
	 * 获取大类：小数据
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws ManagerException
	 */
	public List<CategorySimpleDto> findCategory4Simple(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
}