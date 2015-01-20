/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillOmOutstockDtlMapper
 * @author luo.hl
 * @date  Fri Oct 18 16:35:13 CST 2013
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
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillOmOutstockDtlMapper extends BaseCrudMapper {

    /**
     * 移库确认\回单调用存储过程
     * 
     * @param map
     * @throws DaoException
     */
    public void procOmPlanOutStockDtlQuery(Map<String, String> map)
	    throws DaoException;

    /**
     * 查询客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectStoreNo(
	    @Param("dtl") BillOmOutstockDtl dtl) throws DaoException;

    /**
     * 查询明细 有客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectStockDtl(
	    @Param("dtl") BillOmOutstockDtl dtl) throws DaoException;

    /**
     * 查询明细无客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectStockDtlNoStoreNo(
	    @Param("dtl") BillOmOutstockDtl dtl) throws DaoException;

    /**
     * 查询拣货单中需要复核的商品信息
     * 
     * @param params
     * @return
     */
    public List<BillOmOutstockDtlDto> selectRecheckOutstockItem(
	    @Param("params") Map<String, Object> params) throws DaoException;

    /**
     * 查询拣货单分页总数
     * 
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int selectBillOmOutstockCount(
	    @Param("params") Map<String, Object> params,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 查询拣货单分页
     * 
     * @param page
     * @param orderByField
     * @param orderBy
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectBillOmOutstockByPage(
	    @Param("page") SimplePage page,
	    @Param("orderByField") String orderByField,
	    @Param("orderBy") String orderBy,
	    @Param("params") Map<String, Object> params,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 根据参数查询客户信息
     * 
     * @param params
     *            outstockNo locno
     * @param authorityParams TODO
     * @return
     */
    public List<Store> selectStoreByParam(
	    @Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
    
    /**
     * 根据波次号查询拣货单明细
     * 
     * @param outstockDtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectOutstockDtlByLocateNo(
	    BillOmOutstockDtl outstockDtl) throws DaoException;
    
    /**
     * 根据波次号客户查询拣货单明细
     * 
     * @param outstockDtl
     * @return
     * @throws DaoException
     */
	public List<BillOmOutstockDtl> selectOutstockDtl4Recheck(@Param("params") Map<String, Object> params)
			throws DaoException;
	
	
    /**
     * 拣货波次关闭 时，查询已拣货未复核的数量
     * @param params
     * @return
     * @throws DaoException
     */
	public List<BillOmOutstockDtl> selectPickNoRecheckDtl(@Param("params") Map<String, Object> params)
			throws DaoException;
	
	
	/**
     * 根据波次号客户查询拣货单明细
     * 
     * @param outstockDtl
     * @return
     * @throws DaoException
     */
	public List<BillOmOutstockDtl> selectOutstockDtl4RecheckReduceNum(@Param("params") Map<String, Object> params)
			throws DaoException;

    /**
     * 查询波次下的所有需要回写的拣货单
     * 
     * @param outstockDtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectOutstockDtlByLocateNoGroupBy(
	    @Param("params") BillOmOutstockDtl outstockDtl) throws DaoException;

    /**
     * 查询需要删除的拣货单号
     * 
     * @param outstockDtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectOutstockNoByLocateNoGroupBy(
	    @Param("params") BillOmOutstockDtl outstockDtl) throws DaoException;

    public SumUtilMap<String, Object> selectSumQty(
	    @Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams);

    public List<BillOmOutstockDtlDto> selectAllDetail(
	    @Param("params") BillOmOutstock stock) throws DaoException;
    
    
    
    
    /**
     * 查询复核的拣货单明细分页总数
     * 
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int selectRecheckOutstockItemCount(
	    @Param("params") Map<String, Object> params,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 查询复核的拣货单明细分页
     * 
     * @param page
     * @param orderByField
     * @param orderBy
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtlDto> selectRecheckOutstockItemByPage(
	    @Param("page") SimplePage page,
	    @Param("orderByField") String orderByField,
	    @Param("orderBy") String orderBy,
	    @Param("params") Map<String, Object> params,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;
    
    
    /**
     * 查询复核拣货单明细合计
     * @param map
     * @param authorityParams TODO
     * @return
     * @throws DaoException
     */
    public SumUtilMap<String, Object> selectRecheckOutstockItemSumQty(
    	    @Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
    
    
    /**
	 * 查询客户库存锁定分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectConContentGroupByCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询客户库存锁定分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDtlDto> selectConContentGroupByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	
	/**
	 * 查询客户库存锁定明细分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectMoveStockGroupByCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询客户库存锁定明细分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDtlDto> selectMoveStockGroupByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
    
	 /**
     * 查询即时移库明细合计
     * @param map
	 * @param authorityParams TODO
     * @return
     * @throws DaoException
     */
    public SumUtilMap<String, Object> selectMoveStockSumQty(
    	    @Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
    
    /**
     * 即时移库
     * 
     * @param map
     * @throws DaoException
     */
    public void procImmediateMoveStock(Map<String, String> map) throws DaoException;
    
    /**
     * 自由上架校验储位的合法性
     * 
     * @param map
     * @throws DaoException
     */
    public void procValidteCellNo(Map<String, String> map) throws DaoException;
    
    /**
     * 移库回单明细关联barcode
     * @param map
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtlDto> selectOutstockDtlAndBarcode(@Param("params")Map<String, Object> map) throws DaoException;
    
    /**
     * 批量更新移库回单明细，将实际移库数量改为等于计划数量
     * @param dtl
     * @return
     */
    public int updateRealQtyByPrimaryKeySelective(BillOmOutstock billOmOutstock);
    
    /**
     * 查看移库回单是否被RH操作
     * @param billOmOutstock
     * @return
     * @throws DaoException
     */
	public int selectIsOperateByRF(BillOmOutstock billOmOutstock) throws DaoException;
	
	/**
     * 批量更新移库回单明细，将下架移库数量改为等于实际数量
     * @param dtl
     * @return
     */
    public int updateRealQtyEqInstockQty(BillOmOutstock billOmOutstock);
    
    /**
     * 拣货回单按计划保存
     * @param instock
     * @return
     * @throws DaoException
     */
    public int saveByPlan(BillOmOutstock instock) throws DaoException;
    
    /**
     * 是否存在realyQty大于0的数据
     * @param instock
     * @return
     * @throws DaoException
     */
    public int selectCheckDtlRealQty(BillOmOutstock instock) throws DaoException;
    
    /**
     * 是否存在realyQty等于0的数据
     * @param instock
     * @return
     * @throws DaoException
     */
    public int selectCheckDtlRealQtyEq(BillOmOutstock instock) throws DaoException;
    
    /**
     * 验证RF数量和实际数量是否正确
     * @param instock
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectOutstockDtlCheckoedQty(BillOmOutstock instock) throws DaoException;
    
    /**
     * 根据波茨查询拣货单明细都数量
     * @param outstockDtl
     * @return
     * @throws DaoException
     */
    public int selectOmOutstockDtlNumByLocateNo(BillOmLocate billOmLocate) throws DaoException;
    
    /**
	 * 查询收货单对应的品牌库
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<String> selectSysNo(@Param("params") Map<String, String> map) throws DaoException;
	
	/**
	 *  查询明细中商品的尺码类别 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<String> selectItemSizeKind(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public int selectBoxQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<BillOmOutstockDtl> selectItemDetailByGroup(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams, @Param("page") SimplePage page)
			throws DaoException;

	public List<BillOmOutstockDtl> selectDetailBySizeNo(@Param("params") Map<String, Object> map) throws DaoException;
	
	public int selectItemDetailByGroupCount(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 验证指定的拣货人是否为空
	 * @param instock
	 * @return
	 * @throws DaoException
	 */
	public int selectCheckAssignName(BillOmOutstock instock) throws DaoException;
	
	/**
	 * 如果实际拣货人为空,更新指定拣货人为实际拣货人
	 * @param instock
	 * @throws DaoException
	 */
	public void updateOutstockName4AssignName(BillOmOutstock instock) throws DaoException;
	
	/**
	 * 查询复核数汇总
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int selectOutstockDtlRecheckQtySum(@Param("params") Map<String, Object> map) throws DaoException;
	
}