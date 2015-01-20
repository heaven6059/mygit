package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.enums.CacheTypeEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.CacheManage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.AuthorityUserBrandEnums;
import com.yougou.logistics.uc.client.mapper.AuthorityUserBrandMapper;
import com.yougou.logistics.uc.common.api.model.AuthorityUserBrand;

/**
 * 用户 查询所拥有的品牌权限
 * @author wei.b
 * @date  Mon Sep 23 14:52:10 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("authorityUserBrandService")
class AuthorityUserBrandServiceImpl extends BaseCrudServiceImpl implements AuthorityUserBrandService {
    @Resource
    private AuthorityUserBrandMapper authorityUserBrandMapper;

    @Override
    public BaseCrudMapper init() {
        return null;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthorityUserBrand> findByUserId(String userId,Integer systemId,Integer areaSystemId) throws ServiceException {
		//查询缓存
		Object obj=CacheManage.get(userId,CacheTypeEnum.AUTHORITY_BRAND_QUERY);
		if(null!=obj)
			return (List<AuthorityUserBrand>)obj;
		
		try {
			AuthorityUserBrand model=new AuthorityUserBrand();
			model.setUserId(userId);
			model.setSystemId(systemId);
			model.setAreaSystemId(areaSystemId);
			model.setStatus(AuthorityUserBrandEnums.STATUS1.getStatus());
			List<AuthorityUserBrand> userBrandList=this.authorityUserBrandMapper.selectByModel(model);
			//查询没有就放到缓存，缓存30分钟
			//CacheManage.put(userId, userBrandList,CacheTypeEnum.AUTHORITY_BRAND_QUERY);
			return userBrandList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<String> findByUserIdBrandList(String userId,Integer systemId,Integer areaSystemId) throws ServiceException {
		List<String> brandList=null;
		try {
			List<AuthorityUserBrand> userBrandList=this.findByUserId(userId,systemId,areaSystemId);
			brandList=new ArrayList<String>(userBrandList.size());
			for (AuthorityUserBrand authorityUserBrand : userBrandList) {
				brandList.add(authorityUserBrand.getBrandNo());
			}
			return brandList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<String> findByUserIdPartSysNoList(String userId,Integer systemId,Integer areaSystemId) throws ServiceException {
		List<String> brandList=null;
		try {
			List<AuthorityUserBrand> userBrandList=this.findByUserId(userId,systemId,areaSystemId);
			brandList=new ArrayList<String>(userBrandList.size());
			Set<String> hasAdd =new HashSet<String>();
			String sysNo;
			for (AuthorityUserBrand authorityUserBrand : userBrandList) {
				sysNo=authorityUserBrand.getSysNo();
				if(hasAdd.contains(sysNo)){
					continue;
				}else{
					brandList.add(sysNo);
					hasAdd.add(sysNo);
				}				
			}
			return brandList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
    
}