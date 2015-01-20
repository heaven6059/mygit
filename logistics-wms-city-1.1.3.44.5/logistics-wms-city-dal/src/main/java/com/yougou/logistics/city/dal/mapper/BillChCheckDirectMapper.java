/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillChCheckDirectMapper
 * @author luo.hl
 * @date  Thu Dec 05 14:58:18 CST 2013
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
package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillChCheckDirectBoxDto;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillChCheckDirectMapper extends BaseCrudMapper {

    /**
     * 根据商品获取定位信息
     * 
     * @param params
     * @param authorityParams TODO
     * @return
     */
    public List<BillChCheckDirect> selectDirectByItem(
	    @Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
    
    /**
     * 根据商品获取定位信息 容器记账-零散
     * 
     * @param params
     * @param authorityParams TODO
     * @return
     */
    public List<BillChCheckDirect> selectDirectByItem4RQLS(
	    @Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
    
    
    /**
     * 根据商品获取定位信息  容器记账-整箱
     * 
     * @param params
     * @param authorityParams TODO
     * @return
     */
    public List<BillChCheckDirect> selectDirectByItem4RQZX(
	    @Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
    
    
    /**
     * 查询储位号查询箱子及其数量
     * @param params
     * @param authorityParams
     * @return
     */
    public List<BillChCheckDirectBoxDto> selectDirectBoxNoList(
	    @Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
    
    
    /**
     * 查询储位信息，根据计划单号，储位，属性，品质进行分组
     * @param params
     * @return
     * @throws DaoException
     */
    public List<BillChCheckDirect> selectDirectCellNoByGroup(@Param("params") Map<String, Object> params)throws DaoException;

    
    /**
     * 查询定位明细的箱号
     * @param params
     * @param authorityParams
     * @return
     */
    public List<BillChCheckDirect> selectDirectLableNo(@Param("params") Map<String, Object> params);
    
    
    /**
     * 根据储位获取定位信息
     * 
     * @param params
     * @param authorityParams TODO
     * @return
     */
    public List<BillChCheckDirect> selectDirectByCell(
	    @Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
    
    
    /**
     * 根据储位获取定位信息-零散
     * 
     * @param params
     * @param authorityParams TODO
     * @return
     */
    public List<BillChCheckDirect> selectDirectByCell4RQLS(
	    @Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
    
    
    /**
     * 根据储位获取定位信息-整箱
     * 
     * @param params
     * @param authorityParams TODO
     * @return
     */
    public List<BillChCheckDirect> selectDirectByCell4RQZX(
	    @Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
    
    /**
     * 查询盘点定位和库存信息
     * 
     * @param params
     * @return
     */
    public List<BillChCheckDirectDto> selectDirectAndContent(
	    @Param("params") Map<String, Object> params);
    
    /**
     * 查询定位的储位编码
     * 
     * @param params
     * @return
     */
    public List<String> selectDirectCellNo(
	    @Param("params") Map<String, Object> params);

    /**
     * 出盘发单查询定位表信息数量
     * 
     * @param dto
     * @return
     */
    public int selectDirectCount(@Param("dto") BillChCheckDirectDto dto)
	    throws DaoException;

    /**
     * 出盘发单查询定位表信息
     * 
     * @param dto
     * @param orderBy TODO
     * @return
     */
    public List<BillChCheckDirectDto> selectDirectList(
	    @Param("dto") BillChCheckDirectDto dto,
	    @Param("orderBy")String orderBy, @Param("page") SimplePage page) throws DaoException;

    public Integer selectAllStockCount(@Param("dto") BillChCheckDirectDto dto)
	    throws DaoException;

    public Integer selectAllCellCount(@Param("dto") BillChCheckDirectDto dto)
	    throws DaoException;

    /**
     * 查询定位表中存在的商品的品牌
     * 
     * @param dto
     * @return
     * @throws DaoException
     */
    public List<Brand> selectBrandInDirect(
	    @Param("dto") BillChCheckDirectDto dto) throws DaoException;

    /**
     * 根据储位分组
     * 
     * @param dto
     * @return
     * @throws DaoException
     */
    public List<BillChCheckDirectDto> selectDirectListGroupByCellNo(
	    @Param("dto") BillChCheckDirectDto dto) throws DaoException;

    /**
     * 根据通道分组
     * 
     * @param dto
     * @return
     * @throws DaoException
     */
    public List<BillChCheckDirectDto> selectDirectListGroupByStockNo(
	    @Param("dto") BillChCheckDirectDto dto) throws DaoException;

    /**
     * 查询所有定位信息
     * 
     * @param dto
     * @return
     * @throws DaoException
     */
    public List<BillChCheckDirectDto> selectDirectListAll(
	    @Param("dto") BillChCheckDirectDto dto) throws DaoException;
    
    
    /**
     * 查询零散的商品的定位信息
     * @param dto
     * @return
     * @throws DaoException
     */
    public List<BillChCheckDirectDto> selectDirectList4Single(@Param("dto") BillChCheckDirectDto dto) throws DaoException;
    
    /**
     * 容器记账，查询定位容器中的箱的明细信息
     * @param map
     * @return
     * @throws DaoException
     */
    public List<BillChCheckDirectDto> selectDirectBoxInfo(@Param("param") Map<String, Object> map) throws DaoException;


    /**
     * 根据通道储位创建单据
     * 
     * @param dto
     * @return
     * @throws DaoException
     */
    public List<BillChCheckDirectDto> selectDirectListGroupByCellNoInStockNo(
	    @Param("dto") BillChCheckDirectDto dto,
	    @Param("list") List<BillChCheckDirectDto> stockList)
	    throws DaoException;

    public List<BillChCheckDirect> selectPlanNo(
	    @Param("param") Map<String, String> map,
	    @Param("page") SimplePage page) throws DaoException;

    public int selectPlanNoCount(@Param("param") Map<String, String> map)
	    throws DaoException;

    /*
     * 批量更新状态
     */
    public int updateStatusBatch(@Param("dto") BillChCheckDirectDto dto)
	    throws DaoException;

    /**
     * 批量插入定位信息
     * 
     * @param list
     */
    public void batchInsertDtl(List<BillChCheckDirect> list);

    /**
     * 批量更新状态
     * 
     * @param list
     */
    public void batchUpdate4Status(List<BillChCheckDirect> list);

    public SumUtilMap<String, Object> selectSumQty(
	    @Param("params") Map<String, Object> map, @Param("authorityParams")AuthorityParams authorityParams);
	
	/**
	 * 当全盘时，检查盘点计划中的商品是否可以盘点
	 * @param params
	 * @return
	 */
	public Integer directCheck(@Param("params") Map<String, Object> params);
}