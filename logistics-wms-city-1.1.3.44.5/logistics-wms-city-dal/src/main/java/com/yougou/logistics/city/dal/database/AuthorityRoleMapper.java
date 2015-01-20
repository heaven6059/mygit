package com.yougou.logistics.city.dal.database;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AuthorityRole;
/**
 * 权限模块
 * @author wei.hj
 *
 */
public interface AuthorityRoleMapper extends BaseCrudMapper{
	
    int removeSelective(Long roleId);

    int insertSelective(AuthorityRole record);

    AuthorityRole selectByPrimaryKey(Long roleId);

    int updateSelective(AuthorityRole record);
    
    List<AuthorityRole> queryRoleList(AuthorityRole role);
    
    int removeAuthorityRoleMenu(Long roleId);

    int insertAuthorityRoleMenu(@Param("roleId") Long roleId,@Param("menuId") Long menuId);
    
    List<AuthorityRole> queryMyRoleByUserId(Long userId);
    
    List<AuthorityRole> queryNoMyRoleByUserId(Long userId);
    


}