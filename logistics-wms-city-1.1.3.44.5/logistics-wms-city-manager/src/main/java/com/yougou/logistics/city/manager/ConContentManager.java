package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.ConContentDto;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 14:46:27 CST 2013
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
public interface ConContentManager extends BaseCrudManager {
	
	/**
	 * 查询库存分页总数
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public int findCountMx(@Param("params") ConContentDto conContentDto, AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 查询库存分页
	 * @param page
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public List<ConContentDto> findConContentByPage(@Param("page") SimplePage page,@Param("params") ConContentDto conContentDto, AuthorityParams authorityParams)throws ManagerException;
	
	
	/**
	 * 查询即时库存
	 * @param cc
	 * @param authorityParams
	 * @param all TODO
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public Map<String,Object> findInstantConContentByPage(ConContentDto cc, AuthorityParams authorityParams, boolean all)throws ManagerException;
	public int findInstantConContentCount(ConContentDto cc, AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 分仓库存明细表查询
	 * @param ccd
	 * @param authorityParams
	 * @param all TODO
	 * @return
	 * @throws ManagerException
	 */
	public Map<String,Object> findDivideLocConContentByPage(ConContentDto ccd, AuthorityParams authorityParams, boolean all)throws ManagerException;
}