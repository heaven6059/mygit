package com.yougou.logistics.city.dal.database;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.SystemUserDto;
import com.yougou.logistics.city.common.model.SystemUser;

public interface SystemUserMapper  extends BaseCrudMapper{
	SystemUser selectByPrimaryKey(Long userid);
	List<SystemUser> querySystemUserList(SystemUser systemUser);
    int removeSystemUser(Long userid);
    int insertSystemUser(SystemUser record);
    int updateSystemUser(SystemUser record);
    
    int removeSystemUserRole(Long userId);

    int insertSystemUserRole(@Param("userId") Long userId,@Param("roleId") Long roleId);
    
    
    /**
     * 查询系统用户分页总数量
     * @param systemUserDto
     * @return
     */
    public int selectSystemUserCount(@Param("params") SystemUserDto systemUserDto) throws DaoException;
    
    /**
     * 查询系统用户列表(带分页)
     * @param page
     * @param systemUserDto
     * @return
     */
    public List<SystemUserDto> selectSystemUserByPage(@Param("page") SimplePage page,@Param("params") SystemUserDto systemUserDto) throws DaoException;
    
    public List<SystemUser> querySystemUserListByRoleId(SystemUser systemUser)throws DaoException;

}