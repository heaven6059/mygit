package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.city.common.dto.AuthorityResourcesDTO;
import com.yougou.logistics.city.common.model.AuthorityRole;
import com.yougou.logistics.city.common.model.SystemUser;

/**
 * 系统设置功能Manager管理类
 * @author wei.hj
 *
 */
public interface SystemSettingManager {

   
	/**
	 * 角色模块管理功能...being
	 */
	 
	    int removeRole(Long roleId);

	    AuthorityRole addRole(AuthorityRole record) throws ManagerException;

	    AuthorityRole selectRoleById(Long roleId);

	    AuthorityRole updateRole(AuthorityRole record);
	    
	    List<AuthorityRole> queryRoleList(AuthorityRole role);
	    
	    public AuthorityResourcesDTO queryAllAuthorityResourcesRefRoleId(Long menuId,Long roleId) throws ManagerException;
	
	    public int allotAuthorityRes(Long roleId,String [] resArry) throws ManagerException ;
	/**
	  * 角色管理功能....end	
	*/	
	    
    /**
     * 用户模块管理功能...being
     */
	    public List<SystemUser> querySystemUserList(SystemUser systemUser); 
	    SystemUser querySystemUserById(Long userid);
	    int removeSystemUser(Long userid);
	    int addSystemUser(SystemUser record);
	    int updateSystemUser(SystemUser record); 
	    
	    List<AuthorityRole> queryMyRoleByUserId(Long userId);
	    
	    List<AuthorityRole> queryNoMyRoleByUserId(Long userId);
	    
	    public int allotSystemUserRole(Long userId,String [] resArry) throws ManagerException;
	    
	 /**
	  *  用户模块管理功能...end   
	  */
	    

}
