package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Jun 30 17:31:36 CST 2014
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
public interface AuthorityUserinfoService extends BaseCrudService {
	
	/**
	 * 批量插入用户信息
	 * @param list
	 * @return
	 * @throws ServiceException
	 */
	public void insertBatchUserInfo(List<AuthorityUserinfo> list) throws ServiceException;
	
	/**
	 * 查询用户中心的所有用户
	 * @return
	 * @throws DaoException
	 */
	public List<AuthorityUserinfo> selectAuthorityUser4UserCenter() throws ServiceException;
	
	/**
	 * 删除所有的用户信息
	 */
	public void deleteAllAuthorityUserinfo() throws ServiceException;
	/**
	 * 根据数据库和栈缓存获取用户信息
	 * @param locno
	 * @param loginName
	 * @param cache key:locno_loginName
	 * @return
	 */
	public AuthorityUserinfo findUserByCache(String locno,String loginName,Map<String, AuthorityUserinfo> cache);

	public AuthorityUserinfo findUserByLoginName(String locno, String loginName,int systemId, int areaSystemId)throws ServiceException;
}