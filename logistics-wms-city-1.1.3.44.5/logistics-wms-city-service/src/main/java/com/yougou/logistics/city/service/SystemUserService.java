package com.yougou.logistics.city.service;

import java.util.List;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.SystemUserDto;
import com.yougou.logistics.city.common.model.SystemUser;
/**
 * 权限业务逻辑处理
 * @author wei.hj
 *
 */
public interface SystemUserService extends BaseCrudService{
	
	SystemUser querySystemUserById(Long userid);
	List<SystemUser> querySystemUserList(SystemUser systemUser);
    int removeSystemUser(Long userid);
    int insertSystemUser(SystemUser record);
    int updateSystemUser(SystemUser record);
    
    int removeSystemUserRole(Long userId);

    int insertSystemUserRole( Long userId, Long roleId);
    
    /**
     * 查询系统用户分页总数量
     * @param systemUserDto
     * @return
     */
    public int findSystemUserCount(SystemUserDto systemUserDto) throws ServiceException;
    
    /**
     * 查询系统用户列表(带分页)
     * @param page
     * @param systemUserDto
     * @return
     */
    public List<SystemUserDto> findSystemUserByPage(SimplePage page,SystemUserDto systemUserDto) throws ServiceException;
    
    public List<SystemUser> querySystemUserListByRoleId(SystemUser systemUser)throws ServiceException;
    
}
