package com.yougou.logistics.city.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.SystemUserDto;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.dal.database.SystemUserMapper;

@Service("systemUserService")
public class SystemUserServiceImpl extends BaseCrudServiceImpl implements SystemUserService {
	
	@Resource
	private SystemUserMapper systemUserMapper;
	
	@Override
	public BaseCrudMapper init() {
		return systemUserMapper;
	}

	@Override
	public SystemUser querySystemUserById(Long userid) {
		return systemUserMapper.selectByPrimaryKey(userid);
	}

	@Override
	public List<SystemUser> querySystemUserList(SystemUser systemUser) {
		return systemUserMapper.querySystemUserList(systemUser);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public int removeSystemUser(Long userid) {
		return systemUserMapper.removeSystemUser(userid);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public int insertSystemUser(SystemUser record) {
		return systemUserMapper.insertSystemUser(record);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public int updateSystemUser(SystemUser record) {
		return systemUserMapper.updateSystemUser(record);
	}

	@Override
	public int removeSystemUserRole(Long userId) {
		return systemUserMapper.removeSystemUserRole(userId);
	}

	@Override
	public int insertSystemUserRole(Long userId, Long roleId) {
		return systemUserMapper.insertSystemUserRole(userId, roleId);
	}

	@Override
	public List<SystemUser> querySystemUserListByRoleId(SystemUser systemUser)throws ServiceException {
		try{
			return  systemUserMapper.querySystemUserListByRoleId(systemUser);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public int findSystemUserCount(SystemUserDto systemUserDto) throws ServiceException{
		try {
			return systemUserMapper.selectSystemUserCount(systemUserDto);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SystemUserDto> findSystemUserByPage(SimplePage page, SystemUserDto systemUserDto) throws ServiceException {
		try {
			return systemUserMapper.selectSystemUserByPage(page, systemUserDto);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
