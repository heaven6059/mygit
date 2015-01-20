package com.yougou.logistics.city.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.dto.AuthorityResourcesDTO;
import com.yougou.logistics.city.common.model.AuthorityResources;
import com.yougou.logistics.city.dal.database.AuthorityResourcesMapper;

@Service("authorityResourcesService")
public class AuthorityResourcesServiceImpl implements AuthorityResourcesService {

	@Autowired
	private AuthorityResourcesMapper authorityResourcesMapper;
	
	
	
	@Override
	public AuthorityResourcesDTO queryAllAuthorityResources(Long menuId) throws ServiceException {
		AuthorityResources cur=this.authorityResourcesMapper.selectById(menuId);
		
		AuthorityResourcesDTO dto=new AuthorityResourcesDTO();
		try {
			BeanUtils.copyProperties(dto, cur);
			dto.getAttributes().put("url",cur.getMenuUrl());
			generatorTree(cur, dto);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return dto;
	}
	
	private void generatorTree(AuthorityResources cur, AuthorityResourcesDTO dto)
			throws IllegalAccessException, InvocationTargetException {
		List<AuthorityResources> resourceList= this.authorityResourcesMapper.selectByParentId(cur.getMenuId());
		if(resourceList.size()>0){
			for (AuthorityResources ar : resourceList) {
				AuthorityResourcesDTO cdto=new AuthorityResourcesDTO();
				BeanUtils.copyProperties(cdto, ar);
				cdto.getAttributes().put("url",ar.getMenuUrl());
				if(null!=dto.getAuthorityResourcesDTOList()){
					dto.getAuthorityResourcesDTOList().add(cdto);
				}else{
					List<AuthorityResourcesDTO> list=new ArrayList<AuthorityResourcesDTO>();
					list.add(cdto);
					dto.setAuthorityResourcesDTOList(list);
				}
				generatorTree(ar,cdto);
			}
		}
	}

	@Override
	public AuthorityResourcesDTO queryResourceById(Long menuId) throws ServiceException {
		AuthorityResources cur=this.authorityResourcesMapper.selectById(menuId);
		AuthorityResourcesDTO dto=new AuthorityResourcesDTO();
		try {
			dto.getAttributes().put("url",cur.getMenuUrl());
			BeanUtils.copyProperties(dto, cur);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return dto;
	}

	@Override
	public int addResource(AuthorityResources authorityResources) {
		return this.authorityResourcesMapper.insertSelective(authorityResources);
	}
	
	public int updateResource(AuthorityResources authorityResources){
		return this.authorityResourcesMapper.updateSelective(authorityResources);
	}

	@Override
	public int removeResourceById(Long menuId) {
		return this.authorityResourcesMapper.removeById(menuId);
	}
	
	@Override
	public AuthorityResourcesDTO queryAllAuthorityResourcesRefRoleId(Long menuId,Long roleId) throws ServiceException{
        AuthorityResources cur=this.authorityResourcesMapper.selectByIdRefRoleId(menuId, roleId);
		AuthorityResourcesDTO dto=new AuthorityResourcesDTO();
		try {
			BeanUtils.copyProperties(dto, cur);
			dto.getAttributes().put("url",cur.getMenuUrl());
			// 是否为角色有的菜单
			if(cur!=null&&(cur.getCheckstate()!=null&&!cur.getCheckstate().equals("")&&"1".equals(cur.getCheckstate()))){
				dto.setChecked(true);
			}
			generatorTreeRefRoleId(cur, dto,roleId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return dto;
	}
	
	private void generatorTreeRefRoleId(AuthorityResources cur, AuthorityResourcesDTO dto,Long roleId)
			throws IllegalAccessException, InvocationTargetException {
		List<AuthorityResources> resourceList= this.authorityResourcesMapper.selectByParentIdRefRoleId(cur.getMenuId(),roleId);
		if(resourceList.size()>0){
			for (AuthorityResources ar : resourceList) {
				AuthorityResourcesDTO cdto=new AuthorityResourcesDTO();
				BeanUtils.copyProperties(cdto, ar);
				cdto.getAttributes().put("url",ar.getMenuUrl());
				// 是否为角色有的菜单
				if(ar!=null&&(ar.getCheckstate()!=null&&!ar.getCheckstate().equals("")&&"1".equals(ar.getCheckstate()))){
					cdto.setChecked(true);
				}
				if(null!=dto.getAuthorityResourcesDTOList()){
					dto.getAuthorityResourcesDTOList().add(cdto);
				}else{
					List<AuthorityResourcesDTO> list=new ArrayList<AuthorityResourcesDTO>();
					list.add(cdto);
					dto.setAuthorityResourcesDTOList(list);
				}
				generatorTreeRefRoleId(ar,cdto,roleId);
			}
		}
	}
	
}
