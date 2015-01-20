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
import com.yougou.logistics.city.common.dto.baseinfo.BrandDTO;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.dal.database.BrandMapper;

@Service("brandService")
class BrandServiceImpl  extends BaseCrudServiceImpl  implements BrandService {
	@Resource
	private BrandMapper brandMapper;	
	
	@Override
	public BaseCrudMapper init() {
		return brandMapper;
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BrandDTO> queryBrandListByParentNo(String parentNo,AuthorityParams authorityParams) throws ServiceException {
		if (StringUtils.isBlank(parentNo)) {
			parentNo = "0";
		}
		List<BrandDTO> resultList = new ArrayList<BrandDTO>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brandNoHead", parentNo);
		List<Brand> tempList = brandMapper.selectByParams( params,authorityParams);
		if (tempList != null && tempList.size() > 0) {
			for (Brand brand : tempList) {
				BrandDTO brandDTO = new BrandDTO();
				try {
					BeanUtils.copyProperties(brandDTO, brand);
				} catch (Exception e) {
					throw new ServiceException(e);
				}
				int childrenCount = brand.getChildrenCount();
				if (childrenCount > 0) {
					brandDTO.setState("closed");
				} else {
					brandDTO.setState("open");
				}
				resultList.add(brandDTO);
			}
		}
		return resultList;
	}
	
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Brand selectByCateCode(String cateCode,AuthorityParams authorityParams) throws ServiceException {
		Brand cate = null;
		try {
			cate = brandMapper.selectByCode(cateCode,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return cate;
	}

	@Override
	public List<BrandDTO> findBrand(AuthorityParams authorityParams) throws ServiceException {
		
		List<BrandDTO> resultList = new ArrayList<BrandDTO>();
//		Map<String, Object> params = new HashMap<String, Object>();
		
		List<Brand> tempList = brandMapper.selectByParams( null,authorityParams);
		if (tempList != null && tempList.size() > 0) {
			for (Brand brand : tempList) {
				BrandDTO brandDTO = new BrandDTO();
				try {
					BeanUtils.copyProperties(brandDTO, brand);
				} catch (Exception e) {
					throw new ServiceException(e);
				}
				int childrenCount = brand.getChildrenCount();
				if (childrenCount > 0) {
					brandDTO.setState("closed");
				} else {
					brandDTO.setState("open");
				}
				resultList.add(brandDTO);
			}
		}
		return resultList;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<Brand> findMyselfByParams(Map<String,Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		// TODO Auto-generated method stub
		return brandMapper.selectMyselfByParams(params, authorityParams);
	}

	
//	@Override
//	@DataAccessAuth({DataAccessRuleEnum.BRAND})
//	public Brand findById(String cateCode,AuthorityParams authorityParams) throws ServiceException {
//		Brand cate = null;
//		try {
//			cate = brandMapper.findById(cateCode,authorityParams);
//		} catch (Exception e) {
//			throw new ServiceException(e);
//		}
//		return cate;
//	}
}