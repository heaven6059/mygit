package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.SystemUserDto;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 11:32:16 CST 2013
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
public interface SystemUserManager extends BaseCrudManager {
	
	/**
     * 查询系统用户分页总数量
     * @param systemUserDto
     * @return
     */
    public int findSystemUserCount(SystemUserDto systemUserDto) throws ManagerException;
    
    /**
     * 查询系统用户列表(带分页)
     * @param page
     * @param systemUserDto
     * @return
     */
    public List<SystemUserDto> findSystemUserByPage(SimplePage page,SystemUserDto systemUserDto) throws ManagerException;
    
}