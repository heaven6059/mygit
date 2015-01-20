package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.ConContent;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 14:46:27 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface ConContentService extends BaseCrudService {
	
	/**
	 * 查询库存分页总数
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public int findCountMx(ConContentDto conContentDto, AuthorityParams authorityParams)throws ServiceException;
	
	/**
	 * 查询库存分页
	 * @param page
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public List<ConContentDto> findConContentByPage(SimplePage page,ConContentDto conContentDto, AuthorityParams authorityParams)throws ServiceException;
	
	/**
	 * 查询即时库存
	 * @param cc
	 * @param authorityParams
	 * @param all TODO
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public Map<String,Object> findInstantConContentByPage(ConContentDto cc, AuthorityParams authorityParams, boolean all)throws ServiceException;
	public int findInstantConContentCount(ConContentDto cc, AuthorityParams authorityParams)throws ServiceException;
	
	/**
	 * 获取库存 cell_id
	 * @return
	 * @throws ServiceException
	 */
	public int procGetContentCellid() throws ServiceException;
	
	
	/**
	 * 根据商品信息更新库存预上预下数量
	 * @param conContentDto
	 * @return
	 */
	public ConContent findConContentByItemKey(ConContent conContent) throws ServiceException;
	
	/**
	 * 根据商品信息更新库存预上预下数量
	 * @param conContentDto
	 * @return
	 * @throws DaoException
	 */
	public int updateConContentByItemKey(ConContent conContent) throws ServiceException;
	
	/**
	 * 验证查询储位是否存在库存
	 * @param params
	 * @param listCmDefcells
	 * @return
	 * @throws DaoException
	 */
	public List<ConContent> findCmdefCellIsHaveConContent(Map<String,Object> params,List<CmDefcell> listCmDefcells) throws ServiceException;
	
	/**
	 * 功能：扣减源库存  不循环，带cell_ID，没有预下
	 * @param map
	 * @throws DaoException
	 */
	public void procUpdtContentqtyByCellID(Map<String, String> map)throws ServiceException;
	
	/**
	 * 获取库存账户的Seq
	 * @return
	 * @throws DaoException
	 */
	public String getNextvalId() throws ServiceException;
	
	/**
	 * 根据参数查询
	 * @param modelType 固定参数
	 * @param params 页面其他参数
	 * @return
	 * @throws ServiceException
	 */
	public List<ConContentDto> findViewByParams(Map<String,Object> params)throws ServiceException;
	/**
	 * 查询箱库存
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<ConContentDto> selectConBoxViewByParams(Map<String,Object> params)throws ServiceException;
	/**
	 * 分仓库存明细表查询
	 * @param cc
	 * @param authorityParams
	 * @param all TODO
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> findDivideLocConContentByPage(ConContentDto cc, AuthorityParams authorityParams, boolean all)throws ServiceException;
	
	/**
	 * 获取有库存数据的仓库
	 * @return
	 * @throws ServiceException
	 */
	public List<String> getLocnoByContent() throws ServiceException;
	/**
	 * 更新库存状态
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int modifyStatus(Map<String, Object> params) throws ServiceException;
	
	
	public List<ConContentDto> selectConContent(ConContentDto conContentDto, AuthorityParams authorityParams) throws ServiceException;
}