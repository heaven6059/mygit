/*
 b* 类名 com.yougou.logistics.city.dal.database.ConContentMapper
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
package com.yougou.logistics.city.dal.database;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.ConContent;

public interface ConContentMapper extends BaseCrudMapper {
	
	/**
	 * 查询库存分页总数
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public int selectCountMx(@Param("params") ConContentDto conContentDto,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 查询库存分页
	 * @param page
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public List<ConContentDto> selectConContentByPage(@Param("page") SimplePage page,@Param("params") ConContentDto conContentDto,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 查询即时库存
	 * @param page
	 * @param cc
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<ConContentDto> selectConContentByItemNo(@Param("dto")ConContentDto cc,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	public List<ConContentDto> selectConContentGroupBy(@Param("page") SimplePage page,@Param("dto")ConContentDto cc,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	public int selectConContentCount(@Param("dto")ConContentDto cc,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	public List<ConContentDto> selectInstantConContentSum(@Param("dto")ConContentDto cc,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	
	/**
	 * 获取库存 cell_id
	 * @return
	 */
	public void selectContentCellid(Map<String, BigDecimal> map) throws DaoException;
	
	/**
	 * 根据商品信息查询库存预上预下数量
	 * @param conContentDto
	 * @return
	 */
	public ConContent selectConContentByItemKey(ConContent conContent) throws DaoException;
	
	/**
	 * 根据商品信息更新库存预上预下数量
	 * @param conContentDto
	 * @return
	 * @throws DaoException
	 */
	public int updateConContentByItemKey(ConContent conContent) throws DaoException;
	
	/**
	 * 验证查询储位是否存在库存
	 * @param params
	 * @param listCmDefcells
	 * @return
	 * @throws DaoException
	 */
	public List<ConContent> selectCmdefCellIsHaveConContent(@Param("params")Map<String,Object> params,@Param("list")List<CmDefcell> listCmDefcells) throws DaoException;
	
	/**
	 * 功能：扣减源库存  不循环，带cell_ID，没有预下
	 * @param map
	 * @throws DaoException
	 */
	public void procUpdtContentqtyByCellID(Map<String, String> map)throws DaoException;
	
	/**
	 * 获取库存账户的Seq
	 * @return
	 * @throws DaoException
	 */
	public String getNextvalId() throws DaoException;
	
	public List<ConContentDto> selectViewByParams(@Param("params")Map<String,Object> params);
	
	public List<ConContentDto> selectConBoxViewByParams(@Param("params")Map<String,Object> params);
	/**
	 * 分仓库存明细表(分组查询总数量)
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectCount4DivideLocConContent(@Param("params")ConContentDto params,@Param("authorityParams") AuthorityParams authorityParams);
	/**
	 * 分仓库存明细表(分组分页查询)
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
    public List<ConContentDto> selectByPage4DivideLocConContent(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")ConContentDto params,@Param("authorityParams") AuthorityParams authorityParams);
    /**
     * 分仓库存明细表(查询尺码及数量详情)
     * @param cc
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public List<ConContentDto> selectSizeAndNum4DivideLocConContent(@Param("params")ConContentDto cc,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 获取有库存数据的仓库编码
	 * @return
	 * @throws DaoException
	 */
	public List<String> getLocnoByContent() throws DaoException;
	/**
	 * 更新库存状态()
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public int updateStatus(@Param("params") Map<String, Object> params)throws DaoException;
	
	
	public List<ConContentDto> selectConContent(@Param("params") ConContentDto conContentDto,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
}