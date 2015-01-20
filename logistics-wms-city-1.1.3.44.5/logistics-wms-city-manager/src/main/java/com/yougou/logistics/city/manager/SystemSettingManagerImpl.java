package com.yougou.logistics.city.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.dto.AuthorityResourcesDTO;
import com.yougou.logistics.city.common.model.AuthorityRole;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.service.AuthorityResourcesService;
import com.yougou.logistics.city.service.AuthorityRoleService;
import com.yougou.logistics.city.service.SystemUserService;
/**
 * 系统设置功能Manager管理类
 * @author wei.hj
 *
 */

@Service("systemSettingManager")
public class SystemSettingManagerImpl implements SystemSettingManager {
	
	
	
	@Autowired
	private AuthorityRoleService authorityRoleService;
	
	@Autowired
	private AuthorityResourcesService authorityResourcesService;
	
	@Autowired
	private SystemUserService systemUserService;
	
	
	
	/**
	 * 角色模块管理功能..... begin
	 */
	@Override
	public int removeRole(Long roleId) {
		return this.authorityRoleService.removeRole(roleId);
	}

	@Override
	public AuthorityRole addRole(AuthorityRole record) throws ManagerException {
		try {
			this.authorityRoleService.addRole(record);
			AuthorityRole role=this.authorityRoleService.queryRoleById(record.getRoleId());
			return role;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public AuthorityRole selectRoleById(Long roleId) {
		return this.authorityRoleService.queryRoleById(roleId);
	}

	@Override
	public AuthorityRole updateRole(AuthorityRole record) {
		this.authorityRoleService.updateRole(record);
		AuthorityRole role=this.authorityRoleService.queryRoleById(record.getRoleId());
		return role;
	}
	
	@Override
	public List<AuthorityRole> queryRoleList(AuthorityRole role){
		return this.authorityRoleService.queryRoleList(role);
	}
	

	@Override
	public AuthorityResourcesDTO queryAllAuthorityResourcesRefRoleId(Long menuId,Long roleId) throws ManagerException{
		try {
			return authorityResourcesService.queryAllAuthorityResourcesRefRoleId(menuId, roleId);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	 public int allotAuthorityRes(Long roleId,String [] resArry) throws ManagerException {
		int flag=1;
		try{
			if(resArry!=null&&resArry.length>0){
				 // 1.先按角色ID把数据库里面的记录删除
				this.authorityRoleService.removeAuthorityRoleMenu(roleId);
				//2. 再插入新的角色菜单记录
				for(String temp:resArry){
					if(temp!=null&&temp.trim().length()>0){
						Long tempInt=Long.valueOf(temp);
						this.authorityRoleService.insertAuthorityRoleMenu(roleId, tempInt);
					}
				}
			}
		}catch(Exception e){
			flag=0;
			throw new ManagerException(e);
		}
		return flag=1;
	
	}
	
    /**
	  * 角色模块管理功能....end	
	*/
	
	
    /**
     * 用户模块管理功能...being
     */
	    public List<SystemUser> querySystemUserList(SystemUser systemUser){
	    	return this.systemUserService.querySystemUserList(systemUser);
	    }

	@Override
	public int removeSystemUser(Long userid) {
		return this.systemUserService.removeSystemUser(userid);
	}

	@Override
	public int addSystemUser(SystemUser record) {
		return this.systemUserService.insertSystemUser(record);
	}

	@Override
	public int updateSystemUser(SystemUser record) {
		return this.systemUserService.updateSystemUser(record);
	}
	
	@Override
	public SystemUser querySystemUserById(Long userid){
		return this.systemUserService.querySystemUserById(userid);
	}
	
	@Override
    public List<AuthorityRole> queryMyRoleByUserId(Long userId){
    	return this.authorityRoleService.queryMyRoleByUserId(userId);
    }
	@Override
    public List<AuthorityRole> queryNoMyRoleByUserId(Long userId){
		return this.authorityRoleService.queryNoMyRoleByUserId(userId);
    }
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	 public int allotSystemUserRole(Long userId,String [] resArry) throws ManagerException {
		int flag=1;
		try{
			if(resArry!=null&&resArry.length>0){
				 // 1.先按用户ID把数据库里面的记录删除
				this.systemUserService.removeSystemUserRole(userId);
				//2. 再插入新的用户菜单记录
				for(String temp:resArry){
					if(temp!=null&&temp.trim().length()>0){
						Long tempInt=Long.valueOf(temp);
						this.systemUserService.insertSystemUserRole(userId, tempInt);
					}
				}
			}
		}catch(Exception e){
			flag=0;
			throw new ManagerException(e);
		}
		return flag=1;
	
	}
	    
	 /**
	  *  用户模块管理功能...end   
	  */

}
