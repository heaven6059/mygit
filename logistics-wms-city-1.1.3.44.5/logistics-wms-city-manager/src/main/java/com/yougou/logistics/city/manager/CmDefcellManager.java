package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellSimple;
import com.yougou.logistics.city.common.model.SystemUser;

/**
 * 
 * 储位manager
 * 
 * @author qin.dy
 * @date 2013-9-26 下午5:22:46
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefcellManager extends BaseCrudManager {

	public Map<String, Object> deleteBatch(String locno, List<CmDefcell> listCmDefcells) throws ManagerException;

	/**
	 * 查询分货储位
	 * @param defcell
	 * @return
	 */
	public List<CmDefcell> findCmDefcellByArea(CmDefcell defcell) throws ManagerException;
	
	/**
	 * 允许整储位调整的储位查询
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<CmDefcell> findCmDefcell4Adj(SimplePage page,Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException;
	
	public int findCmDefcell4AdjCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 允许盘点的储位查询
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<CmDefcell> findCmDefcell4Plan(SimplePage page,Map<String,Object> params) throws ManagerException;
	
	public int findCmDefcell4PlanCount(Map<String,Object> params)throws ManagerException;

	public int queryContent(CmDefcell defcell) throws ManagerException;

	public CmDefcell selectCellNo4BillHmPlan(CmDefcell defcell) throws ManagerException;
	
	public boolean modifyCmDefcell(CmDefcell cmDefcell) throws ManagerException;
	/**
	 * 批量修改状态
	 * @param keys cellNo|cellStatus
	 * @param locno
	 * @param status 目标状态
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public String batchModifyStatus(String keys,String status,SystemUser user) throws ManagerException;
	/**
	 * 根据通道查询货主
	 * @param stockNo
	 * @return
	 * @throws ManagerException
	 */
	public String findOwnerByStock(CmDefcell cmDefcell) throws ManagerException;
	

	public List<CmDefcellSimple> findSimple(Map<String,Object> params) throws ManagerException;
}