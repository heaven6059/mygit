package com.yougou.logistics.city.dal.database;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.city.common.model.AuthorityResources;
/**
 * 菜单管理
 * @author wei.hj
 *
 */
public interface AuthorityResourcesMapper{
	
	AuthorityResources selectById(Long id);
	
	List<AuthorityResources> selectByParentId(Long parentId);
	
    int insert(AuthorityResources record);

    int insertSelective(AuthorityResources record);
    
    int updateSelective(AuthorityResources record);
    
    int removeById(Long id);
    
	AuthorityResources selectByIdRefRoleId(@Param("menuId") Long menuId,@Param("roleId") Long roleId);
	
	List<AuthorityResources> selectByParentIdRefRoleId(@Param("parentId") Long parentId,@Param("roleId") Long roleId);
	
}