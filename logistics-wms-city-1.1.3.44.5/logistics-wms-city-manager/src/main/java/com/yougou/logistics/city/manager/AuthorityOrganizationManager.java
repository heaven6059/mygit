package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.uc.common.api.dto.AuthorityOrganizationDto;
import com.yougou.logistics.uc.common.api.dto.AuthorityRoleWithUserListDto;

/**
 * 机构接口
 * 
 * @author jiang.ys
 * @date 2014-1-24 下午1:35:12
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface AuthorityOrganizationManager {

	/**
	 * 添加机构至用户中心
	 * @param authorityOrganizationDto
	 */
	public boolean addAuthorityOrganization(AuthorityOrganizationDto authorityOrganizationDto);

	/**
	 * 根据机构编码删除机构
	 * @param storeNo
	 * @return
	 */
	public boolean deleteAuthorityOrganization(String storeNo);

	/**
	 * 根据机构编号修改机构
	 * @param authorityOrganizationDto
	 * @return
	 */
	public boolean updateAuthorityOrganization(AuthorityOrganizationDto authorityOrganizationDto);

	/**
	 * 
	 * @param organizNo
	 * @param systemId
	 * @param areaSystemId
	 * @return
	 * @throws ManagerException
	 */
	public List<AuthorityRoleWithUserListDto> findRoleListWithUserListByOrganization(String organizNo,int systemId,int areaSystemId) throws ManagerException;
}
