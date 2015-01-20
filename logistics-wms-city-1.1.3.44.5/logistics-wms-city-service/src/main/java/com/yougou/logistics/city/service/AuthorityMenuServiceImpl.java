package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.AuthorityMenuDto;
import com.yougou.logistics.city.common.dto.AuthorityModuleDto;
import com.yougou.logistics.city.common.enums.AuthorityMenuTypeEnums;
import com.yougou.logistics.city.common.model.AuthorityMenu;
import com.yougou.logistics.city.dal.database.AuthorityMenuMapper;
import com.yougou.logistics.city.dal.database.AuthorityModuleMapper;

@Service("authorityMenuService")
class AuthorityMenuServiceImpl extends BaseCrudServiceImpl implements AuthorityMenuService {
	@Resource
	private AuthorityMenuMapper authorityMenuMapper;
	
	@Resource
	private AuthorityModuleMapper authorityModuleMapper;

	@Override
	public BaseCrudMapper init() {
		return authorityMenuMapper;
	}

	@Override
	public AuthorityMenuDto findAllAuthorityMenu(int menuId) throws ServiceException {
		try {
			AuthorityMenuDto parentDto = this.authorityMenuMapper.selectByPrimaryKey4Dto(menuId);
			if (null != parentDto){
				generatorTree(parentDto,null);
				parentDto.setMenuType(AuthorityMenuTypeEnums.MENU_TYPE.type);
				if(parentDto.getAuthorityMenuDtoList().size()>0){
					parentDto.setMenuIsleaf("false");
				}else{
					parentDto.setMenuIsleaf("true");
				}
			}
			return parentDto;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 递归生成树
	 * @param cur
	 */
	private void generatorTree(AuthorityMenuDto cur,Map<Integer,List<AuthorityMenuDto>> menuMap) {
		List<AuthorityMenuDto> resourceList = this.authorityMenuMapper.selectByParentId(cur.getMenuId());
		if (resourceList.size() > 0) {
			for (AuthorityMenuDto ar : resourceList) {
				ar.setMenuType(AuthorityMenuTypeEnums.MENU_TYPE.type);
				if (null != cur.getAuthorityMenuDtoList()) {
					cur.getAuthorityMenuDtoList().add(ar);
				} else {
					List<AuthorityMenuDto> list = new ArrayList<AuthorityMenuDto>();
					list.add(ar);
					cur.setAuthorityMenuDtoList(list);
				}
				//非叶子
				cur.setMenuIsleaf("false");
				generatorTree(ar,menuMap);
			}
		}else{
			//模块列表
			if(null!=menuMap&&menuMap.size()>0){
				cur.setAuthorityMenuDtoList(menuMap.get(cur.getMenuId()));
			}
			//记录它没有子节点 叶子节点
			cur.setMenuIsleaf("true");
		}
	}

	@Override
	public AuthorityMenu findAuthorityMenuWithIsLeaf(AuthorityMenu authorityMenu) throws ServiceException {
		try {
			authorityMenu=this.authorityMenuMapper.selectByPrimaryKey(authorityMenu);
			if(null!=authorityMenu){
				List<AuthorityMenuDto> list=this.authorityMenuMapper.selectByParentId(authorityMenu.getMenuId());
				authorityMenu.setMenuIsleaf(list.size()>0?"false":"true");
			}
			return authorityMenu;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int findCountByParentId(AuthorityMenu authorityMenu) throws ServiceException {
		try {
			return this.authorityMenuMapper.selectCountByParentId(authorityMenu);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public AuthorityMenuDto findUserHasMenus(int userId,int menuId) throws ServiceException {
		try {
			//查询用户拥有的模块
			List<AuthorityModuleDto> moduleList=this.authorityModuleMapper.selectUserHasModules(userId);
			return builderMenuDto(menuId, moduleList);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 构建菜单
	 * @param menuId
	 * @param moduleList
	 * @return
	 */
	private AuthorityMenuDto builderMenuDto(int menuId, List<AuthorityModuleDto> moduleList) {
		if(null!=moduleList&&moduleList.size()>0){
			//转换module-->menu
			Map<Integer,List<AuthorityMenuDto>> menuMap=transformMenuMap(moduleList);
			//查询所有的菜单
			AuthorityMenuDto parentDto = this.authorityMenuMapper.selectByPrimaryKey4Dto(menuId);
			if (null != parentDto){
				generatorTree(parentDto,menuMap);
				parentDto.setMenuType(AuthorityMenuTypeEnums.MENU_TYPE.type);
				if(parentDto.getAuthorityMenuDtoList().size()>0){
					parentDto.setMenuIsleaf("false");
				}else{
					parentDto.setMenuIsleaf("true");
				}
			}
			//剪枝,去掉无引用菜单
			pruneMenu(parentDto);
			
			return parentDto;
		}
		return null;
	}
	
	/**
	 * 剪枝
	 * @param authorityMenuDto
	 * @return 返回true，表示可以剪枝
	 */
	private boolean pruneMenu(AuthorityMenuDto authorityMenuDto){
		//是否有子节点
		if(null!=authorityMenuDto.getAuthorityMenuDtoList()&&authorityMenuDto.getAuthorityMenuDtoList().size()>0){
			//剪枝无法使用foreach
			Iterator<AuthorityMenuDto> its=authorityMenuDto.getAuthorityMenuDtoList().iterator();
			while (its.hasNext()) {
				//递归下一级
				if(pruneMenu(its.next())){
					its.remove();
				}
			}
			//递归完成后回到父节点,判断是否有子节点,没有
			if(authorityMenuDto.getAuthorityMenuDtoList().size()==0){
				return true;
			}else{
				return false;
			}
		}else{
			//最后一级不是模块，就是无效菜单，需要剪枝
			//无子菜单
			if(null!=authorityMenuDto.getMenuType()&&AuthorityMenuTypeEnums.MENU_TYPE.type.equals(authorityMenuDto.getMenuType())){
				return true;
			}else{
				return false;
			}
		}
	}

	@Override
	public AuthorityMenuDto findHasAdminMenu(int menuId) throws ServiceException {
		List<AuthorityModuleDto> menuList=this.authorityModuleMapper.selectAllMenusWithModules();
		try {
			return builderMenuDto(menuId,menuList);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 挂到菜单叶子节点上,模块转换为菜单
	 * @param moduleList
	 * @return
	 */
	private Map<Integer,List<AuthorityMenuDto>> transformMenuMap(List<AuthorityModuleDto> moduleList){
		Map<Integer,List<AuthorityMenuDto>> menuMap=new HashMap<Integer,List<AuthorityMenuDto>>();
		for (AuthorityModuleDto module : moduleList) {
			if(null==menuMap.get(module.getMenuId())){
				List<AuthorityMenuDto> menuList=new ArrayList<AuthorityMenuDto>();
				menuMap.put(module.getMenuId(),menuList);
				transformMenus(module, menuList);
			}else{
				transformMenus(module,menuMap.get(module.getMenuId()));
			}
		}
		return menuMap;
	}

	private void transformMenus(AuthorityModuleDto module, List<AuthorityMenuDto> menuList) {
		AuthorityMenuDto menu=new AuthorityMenuDto();
		menu.setMenuId(module.getModuleId());
		menu.setMenuName(module.getModuleName());
		menu.setMenuType(AuthorityMenuTypeEnums.MODULE_TYPE.type);
		menu.getAttributes().put("url",module.getModuleUrl());
		menuList.add(menu);
	}
}