package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.SystemUserDto;
import com.yougou.logistics.city.service.SystemUserService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

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
@Service("systemUserManager")
class SystemUserManagerImpl extends BaseCrudManagerImpl implements SystemUserManager {
	
    @Resource
    private SystemUserService systemUserService;

    @Override
    public BaseCrudService init() {
        return systemUserService;
    }

	@Override
	public int findSystemUserCount(SystemUserDto systemUserDto) throws ManagerException{
		try {
			return systemUserService.findSystemUserCount(systemUserDto);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<SystemUserDto> findSystemUserByPage(SimplePage page, SystemUserDto systemUserDto) throws ManagerException{
		try {
			return systemUserService.findSystemUserByPage(page, systemUserDto);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}