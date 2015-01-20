package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AuthorityModule;
import com.yougou.logistics.city.common.vo.AuthorityModuleVo;
import com.yougou.logistics.city.dal.database.AuthorityModuleMapper;

@Service("authorityModuleService")
class AuthorityModuleServiceImpl extends BaseCrudServiceImpl implements AuthorityModuleService {
	@Resource
	private AuthorityModuleMapper authorityModuleMapper;

	@Override
	public BaseCrudMapper init() {
		return authorityModuleMapper;
	}

	@Override
	public AuthorityModuleVo findAllModulesAndUsedModules(int menuId) throws ServiceException {
		try {
			List<AuthorityModule> allModuleList = this.authorityModuleMapper.selectByParams(new AuthorityModule(), null);
			List<AuthorityModule> usedModuleList = this.authorityModuleMapper.selectAllByMenuId(menuId);
			//去除重复的模块
			if(null!=usedModuleList&&usedModuleList.size()>0){
				Map<Integer,AuthorityModule> usedModuleMap=new HashMap<Integer,AuthorityModule>(usedModuleList.size());
				for (AuthorityModule authorityModule : usedModuleList) {
					usedModuleMap.put(authorityModule.getModuleId(),authorityModule);
				}
				
				for (int i = 0; i < allModuleList.size(); i++) {
					if(usedModuleMap.containsKey(allModuleList.get(i).getModuleId())){
						allModuleList.remove(i);
					}
				}
			}
			
			AuthorityModuleVo vo = new AuthorityModuleVo();
			vo.setAllAuthorityModuleList(allModuleList);
			vo.setUsedAuthorityModuleList(usedModuleList);
			return vo;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int findModuleCountByMenuId(int menuId) throws ServiceException {
		try{
			return this.authorityModuleMapper.selectModuleCountByMenuId(menuId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Boolean findModuleIsExistByName(AuthorityModule module) throws ServiceException {
		try{
			Boolean exist=this.authorityModuleMapper.selectModuleIsExistByName(module);
			return null!=exist?true:false;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}