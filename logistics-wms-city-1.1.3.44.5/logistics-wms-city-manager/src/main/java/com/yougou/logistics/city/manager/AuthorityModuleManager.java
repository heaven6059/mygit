package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.AuthorityModule;
import com.yougou.logistics.city.common.vo.AuthorityModuleVo;
import com.yougou.logistics.city.common.vo.AuthorityModuleWithOperationsVo;

/**
 * 
 * 模块管理
 * 
 * @author wei.b
 * @date 2013-8-22 下午2:45:51
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface AuthorityModuleManager extends BaseCrudManager {
	/**
	 * 查询所有模块与菜单下的模块
	 * @param menuId
	 * @return
	 */
	public AuthorityModuleVo findAllModulesAndUsedModules(int menuId) throws ManagerException;
	
	/**
	 * 查询模块是否存在
	 * @param module
	 * @return
	 * @throws ManagerException
	 */
	public Boolean findModuleIsExistByName(AuthorityModule module)throws ManagerException;
	
	/**
	 * 
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<AuthorityModuleWithOperationsVo> findByPageWithOPerations(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ManagerException;
	/**
	 * 
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int saveMoudleWithOperations(Map<CommonOperatorEnum,List<AuthorityModuleWithOperationsVo>> params) throws ManagerException;
	
	
}