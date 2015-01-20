package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.SystemUser;

/**
 * 容器基础信息维护
 * @author wanghb
 * @date   2014-8-5
 * @version 1.1.3.37
 */
public interface BmContainerManager extends BaseCrudManager {
	/**
	 * 批量删除容器
	 * @param locnoStrs
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public int deleteFefloc(String locnoStrs,SystemUser user) throws ManagerException ;
	/**
	 * 保存
	 * @param bmContainer
	 * @throws ManagerException
	 */
	public void addEntity(BmContainer bmContainer)throws ManagerException;
	/**
	 * 批量更新容器
	 * @param bmContainer
	 * @return
	 * @throws ManagerException
	 */
	public int batchUpdate(List<BmContainer> bmContainer)throws ManagerException;
	/**
	 * 判断容器是否占用
	 * @return true：已占用或不存在  false：可用
	 * @throws ManagerException
	 */
	public boolean checkBmContainerStatus(BmContainer bmContainer)throws ManagerException;
	
}