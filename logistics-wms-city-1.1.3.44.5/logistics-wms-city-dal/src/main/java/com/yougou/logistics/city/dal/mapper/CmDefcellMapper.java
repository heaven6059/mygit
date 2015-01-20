package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellSimple;

/**
 * 
 * 储位mapper 
 * 
 * @author qin.dy
 * @date 2013-9-26 下午5:20:49
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefcellMapper extends BaseCrudMapper {

	/**
	 * 查询分货储位
	 * @param defcell
	 * @return
	 */
	public List<CmDefcell> selectCmDefcellByArea(@Param("params") CmDefcell defcell) throws DaoException;

	public List<CmDefcell> selectCmDefcell4Adj(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public int selectCmDefcell4AdjCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<CmDefcell> selectCmDefcell4Plan(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> params) throws DaoException;

	public int selectCmDefcell4PlanCount(@Param("params") Map<String, Object> params) throws DaoException;
	
	public List<CmDefcell> selectCellNoByUserType(@Param("params") Map<String, Object> params) throws DaoException;

	public int queryContent(CmDefcell defcell) throws DaoException;

	public CmDefcell selectCellNo4BillHmPlan(CmDefcell defcell) throws DaoException;

	public List<CmDefcell> select4ReturnedGoods(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 根据通道查询货主
	 * @param stockNo
	 * @return
	 * @throws DaoException
	 */
	public String selectOwnerByStock(@Param("params") CmDefcell cmDefcell) throws DaoException;

	public List<CmDefcellSimple> selectSimple(@Param("params") Map<String, Object> params) throws DaoException;

	public List<CmDefcell> selectByParams4Instock(@Param("params") Map<String, Object> params);
	/**
	 * 转货查询目的储位 ：库区用途8-转货区、库区属性0-作业区、属性类型0-存储区 
	 * @param params
	 * @return
	 */
	public List<CmDefcell> selectDestCell4Convert(@Param("params") Map<String, Object> params);
	
	public String selectJhzcqCellNo(CmDefcell cmDefcell) throws DaoException;
}