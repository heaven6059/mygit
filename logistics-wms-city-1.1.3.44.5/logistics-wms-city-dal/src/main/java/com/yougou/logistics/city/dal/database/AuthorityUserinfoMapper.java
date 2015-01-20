/*
 * 类名 com.yougou.logistics.city.dal.database.AuthorityUserinfoMapper
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;

public interface AuthorityUserinfoMapper extends BaseCrudMapper {
	
	/**
	 * 批量插入用户
	 * @param list
	 * @throws DaoException
	 */
	public void insertBatchUserInfo(List<AuthorityUserinfo> list) throws DaoException;
	
	/**
	 * 查询用户中心的所有用户
	 * @return
	 * @throws DaoException
	 */
	public List<AuthorityUserinfo> selectAuthorityUser4UserCenter() throws DaoException;
	
	/**
	 * 删除所有的用户信息
	 */
	public void deleteAllAuthorityUserinfo() throws DaoException;

	/**
	 * 根据登录名查询用户信息
	 * @param object
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public AuthorityUserinfo selectUserByLoginName(Map<String, Object> params) throws DaoException;
}