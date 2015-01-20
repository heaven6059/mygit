/*
 * 类名 com.yougou.logistics.city.dal.mapper.LoginuserMapper
 * @author luo.hl
 * @date  Mon Sep 23 10:01:03 CST 2013
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

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.Loginuser;

public interface LoginuserMapper extends BaseCrudMapper {
	
	/**
	 * 根据角色ID查询对应下的所有用户信息
	 * @param roleId
	 * @return
	 * @throws DaoException
	 * @author zuo.sw
	 */
	public  List<Loginuser>  getUserListByRoleId(Integer roleId)throws DaoException;
	
}