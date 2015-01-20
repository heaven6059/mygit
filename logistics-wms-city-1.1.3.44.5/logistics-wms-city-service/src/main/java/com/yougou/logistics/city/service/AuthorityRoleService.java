package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityRole;
/**
 * 权限业务逻辑处理
 * @author wei.hj
 *
 */
public interface AuthorityRoleService extends BaseCrudService{
    int removeRole(Long roleId);

    int addRole(AuthorityRole record) throws ServiceException;

    AuthorityRole queryRoleById(Long roleId);

    int updateRole(AuthorityRole record);
    
    List<AuthorityRole> queryRoleList(AuthorityRole role);
    
    int removeAuthorityRoleMenu(Long roleId);

    int insertAuthorityRoleMenu( Long roleId, Long menuId);
    
    List<AuthorityRole> queryMyRoleByUserId(Long userId);
    
    List<AuthorityRole> queryNoMyRoleByUserId(Long userId);
    

    
}
