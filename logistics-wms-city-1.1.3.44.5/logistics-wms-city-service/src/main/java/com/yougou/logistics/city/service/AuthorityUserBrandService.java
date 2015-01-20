package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.uc.common.api.model.AuthorityUserBrand;

/**
 * 请写出类的用途 
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
public interface AuthorityUserBrandService extends BaseCrudService {
	/**
	 * 根据用户编号查询用户所拥有品牌
	 * 将保存缓存
	 * @param userId
	 * @param systemId
	 * @param areaSystemId
	 * @return
	 * @throws ServiceException
	 */
	public List<AuthorityUserBrand> findByUserId(String userId,Integer systemId,Integer areaSystemId)throws ServiceException;
	/**
	 * 查找用户所拥有的品牌
	 * @param userId
	 * @param systemId
	 * @param areaSystemId
	 * @return
	 * @throws ServiceException
	 */
	public List<String> findByUserIdBrandList(String userId,Integer systemId,Integer areaSystemId)throws ServiceException;

	/**
	 * 查找用户所拥有的品牌库（该品牌库下一个或多个品牌有权限）
	 * @param userId
	 * @param systemId
	 * @param areaSystemId
	 * @return
	 * @throws ServiceException
	 */
	public List<String> findByUserIdPartSysNoList(String userId,Integer systemId,Integer areaSystemId)throws ServiceException;
}