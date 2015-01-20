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
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.CategorySimpleDto;
import com.yougou.logistics.city.common.dto.baseinfo.CategoryDTO;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.dal.database.CategoryMapper;

@Service("categoryService")
class CategoryServiceImpl extends BaseCrudServiceImpl implements CategoryService {
	@Resource
	private CategoryMapper categoryMapper;

	@Override
	public BaseCrudMapper init() {
		return categoryMapper;
	}

	@Override
	public List<CategoryDTO> queryCategoryListByParentNo(String parentNo,String sysNo) throws ServiceException {
		if (StringUtils.isBlank(parentNo)) {
			parentNo = "0";
		}
		List<CategoryDTO> resultList = new ArrayList<CategoryDTO>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headCateNo", parentNo);
		params.put("sysNo", sysNo);
		List<Category> tempList = categoryMapper.selectByParams(null, params);
		if (tempList != null && tempList.size() > 0) {
			for (Category category : tempList) {
				CategoryDTO categoryDTO = new CategoryDTO();
				try {
					BeanUtils.copyProperties(categoryDTO, category);
				} catch (Exception e) {
					throw new ServiceException(e);
				}
				if ("0".equals(parentNo) && StringUtils.isNotBlank(category.getSysNoStr())) {
					categoryDTO.setCateName(categoryDTO.getCateName() + "(" + category.getSysNo() + ")");
				}
				int childrenCount = category.getChildrenCount();
				if (childrenCount > 0) {
					categoryDTO.setState("closed");
				} else {
					categoryDTO.setState("open");
				}
				resultList.add(categoryDTO);
			}
		}
		return resultList;
	}

	@Override
	public Category selectByCateCode(String cateCode) throws ServiceException {
		Category cate = null;
		try {
			cate = categoryMapper.selectByCode(cateCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return cate;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<CategorySimpleDto> findCategory4Simple(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return categoryMapper.selectCategory4Simple(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}