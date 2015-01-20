package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellSimple;

/**
 * 
 * 储位service
 * 
 * @author qin.dy
 * @date 2013-9-26 下午5:23:27
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefcellService extends BaseCrudService {

	/**
	 * 查询分货储位
	 * @param defcell
	 * @return
	 */
	public List<CmDefcell> findCmDefcellByArea(@Param("params") CmDefcell defcell) throws ServiceException;
	
	/**
	 * 允许整储位调整的储位查询
	 * @param page
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<CmDefcell> findCmDefcell4Adj(SimplePage page,Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 查询退厂储位
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<CmDefcell> find4ReturnedGoods(Map<String,Object> params) throws ServiceException;
	
	public int findCmDefcell4AdjCount(Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 允许盘点的储位查询
	 * @param page
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<CmDefcell> findCmDefcell4Plan(SimplePage page,Map<String,Object> params) throws ServiceException;
	
	public List<CmDefcell> selectCellNoByUserType(Map<String,Object> params) throws ServiceException;
	
	public int findCmDefcell4PlanCount(Map<String,Object> params) throws ServiceException;

	public int queryContent(CmDefcell defcell) throws ServiceException;

	public CmDefcell selectCellNo4BillHmPlan(CmDefcell defcell) throws ServiceException;
	
	/**
	 * 根据通道查询货主
	 * @param stockNo
	 * @return
	 * @throws ServiceException
	 */
	public String findOwnerByStock(CmDefcell cmDefcell) throws ServiceException;
	

	public List<CmDefcellSimple> findSimple(Map<String,Object> params) throws ServiceException;
	/**
	 * 转货查询目的储位 ：库区用途8-转货区、库区属性0-作业区、属性类型0-存储区 
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<CmDefcell> findDestCell4Convert(Map<String,Object> params) throws ServiceException;

}