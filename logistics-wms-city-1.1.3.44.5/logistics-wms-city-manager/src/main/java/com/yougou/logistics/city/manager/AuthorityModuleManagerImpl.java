package com.yougou.logistics.city.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityModule;
import com.yougou.logistics.city.common.model.AuthorityModuleOperations;
import com.yougou.logistics.city.common.vo.AuthorityModuleVo;
import com.yougou.logistics.city.common.vo.AuthorityModuleWithOperationsVo;
import com.yougou.logistics.city.service.AuthorityModuleOperationsService;
import com.yougou.logistics.city.service.AuthorityModuleService;

@Service("authorityModuleManager")
class AuthorityModuleManagerImpl extends BaseCrudManagerImpl implements AuthorityModuleManager {
	@Resource
	private AuthorityModuleService authorityModuleService;
	@Resource
	private AuthorityModuleOperationsService authorityModuleOperationsService;

	@Override
	public BaseCrudService init() {
		return authorityModuleService;
	}

	@Override
	public AuthorityModuleVo findAllModulesAndUsedModules(int menuId) throws ManagerException {
		try {
			return this.authorityModuleService.findAllModulesAndUsedModules(menuId);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public Boolean findModuleIsExistByName(AuthorityModule module) throws ManagerException {
		try {
			return this.authorityModuleService.findModuleIsExistByName(module);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<AuthorityModuleWithOperationsVo> findByPageWithOPerations(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params) throws ManagerException {
		try {
			//查询模块
			List<AuthorityModule> modules = super.findByPage(page, orderByField, orderBy, params);
			List<AuthorityModuleWithOperationsVo> voList = new ArrayList<AuthorityModuleWithOperationsVo>(modules.size());
			//查询操作权限
			List<AuthorityModuleOperations> operations = this.authorityModuleOperationsService.findHasOperatorModules();
			Map<Integer, List<Integer>> moduleWithOperations = new HashMap<Integer, List<Integer>>();
			for (AuthorityModuleOperations authorityModuleOperations : operations) {
				if (null == moduleWithOperations.get(authorityModuleOperations.getModuleId())) {
					List<Integer> operationList = new ArrayList<Integer>();
					moduleWithOperations.put(authorityModuleOperations.getModuleId(), operationList);
					operationList.add(authorityModuleOperations.getOprationId());
				} else {
					moduleWithOperations.get(authorityModuleOperations.getModuleId()).add(
							authorityModuleOperations.getOprationId());
				}
			}

			for (AuthorityModule m : modules) {
				AuthorityModuleWithOperationsVo mwo = new AuthorityModuleWithOperationsVo();
				voList.add(mwo);
				BeanUtils.copyProperties(mwo, m);
				
				List<Integer> pList=moduleWithOperations.get(m.getModuleId());
				String operationsStr="";
				if(null!=pList){
					for (Integer p : pList) {
						operationsStr+=operationsStr.length()==0?p:","+p;
					}
					mwo.setOperations(operationsStr);
				}
			}
			return voList;
		} catch (ServiceException e) {
			throw new ManagerException(e);
		} catch (ManagerException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new ManagerException(e);
		} catch (InvocationTargetException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int saveMoudleWithOperations(Map<CommonOperatorEnum, List<AuthorityModuleWithOperationsVo>> params)
			throws ManagerException {
		int count=0;
		try {
			count+=super.save(params);
			for (Entry<CommonOperatorEnum, List<AuthorityModuleWithOperationsVo>> param : params.entrySet()) {
				if(CommonOperatorEnum.DELETED.equals(param.getKey())){
					for (AuthorityModuleWithOperationsVo module : param.getValue()) {
						AuthorityModuleOperations mo=new AuthorityModuleOperations();
						mo.setModuleId(module.getModuleId());
						count+=authorityModuleOperationsService.deleteById(mo);
					}
				}
				
				if(CommonOperatorEnum.UPDATED.equals(param.getKey())){
					for (AuthorityModuleWithOperationsVo module : param.getValue()) {
						AuthorityModuleOperations mo=new AuthorityModuleOperations();
						mo.setModuleId(module.getModuleId());
						count+=authorityModuleOperationsService.deleteById(mo);
						
						if(StringUtils.isNotEmpty(module.getOperations())){
							String usedOperations[]=module.getOperations().split(",");
							for (String oId : usedOperations) {
								mo.setOprationId(Integer.parseInt(oId));
								authorityModuleOperationsService.add(mo);
								count++;
							}
						}
					}
				}
				
				if(CommonOperatorEnum.INSERTED.equals(param.getKey())){
					for (AuthorityModuleWithOperationsVo module : param.getValue()) {
						AuthorityModuleOperations mo=new AuthorityModuleOperations();
						mo.setModuleId(module.getModuleId());
						
						if(StringUtils.isNotEmpty(module.getOperations())){
							String usedOperations[]=module.getOperations().split(",");
							for (String oId : usedOperations) {
								mo.setOprationId(Integer.parseInt(oId));
								authorityModuleOperationsService.add(mo);
								count++;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		} catch (ManagerException e) {
			throw e;
		}
		return count;
	}
}