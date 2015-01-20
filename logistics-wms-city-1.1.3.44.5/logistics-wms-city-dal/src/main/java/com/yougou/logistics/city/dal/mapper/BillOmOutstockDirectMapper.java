package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.BillOmOutstockDirect;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillOmOutstockDirectForQuery;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-09 11:09:10
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
public interface BillOmOutstockDirectMapper extends BaseCrudMapper {
	
	/**
	 * 根据出库单类型查询波次号和批次列表
	 * @param authorityParams TODO
	 * @param expType
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect>  queryLocateNoByExpType(@Param("params") BillOmOutstockDirectForQuery vo, @Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	
	/**
	 * 出库单类型,波次号，出库单号，订单号查询波次号和批次列表
	 * @param authorityParams TODO
	 * @param expType
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect>  queryLocateNoByMore(@Param("map") Map<String,Object> map, @Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 根据出库单类型，波次号，批次查询作业类型
	 * @param vo
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect>  queryOperateTypeByParam(@Param("params") BillOmOutstockDirectForQuery vo, @Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 根据出库单类型，波次号，批次，作业类型查询库区信息
	 * @param vo
	 * @param authorityParams TODO
	 * @param orderByField TODO
	 * @param orderBy TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect>  queryAreaInfoByParam(@Param("params") BillOmOutstockDirectForQuery vo, @Param("authorityParams") AuthorityParams authorityParams, @Param("orderByField") String orderByField,@Param("orderBy") String orderBy)throws DaoException;
	
	
	/**
	 * 根据出库单类型，波次号，批次，作业类型查询客户信息
	 * @param vo
	 * @param authorityParams TODO
	 * @param orderByField TODO
	 * @param newParam2 TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect>  queryStoreInfoByParam(@Param("params") BillOmOutstockDirectForQuery vo, @Param("authorityParams") AuthorityParams authorityParams, @Param("orderByField") String orderByField,@Param("orderBy") String orderBy)throws DaoException;
	
	/**
	 * 根据出库单类型，波次号，批次,作业类型 ，客户查询拣货的商品信息
	 * @param vo
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect>  queryOutstockDirectByStore(@Param("params")BillOmOutstockDirectForQuery vo)throws DaoException;
	
	/**
	 * 根据出库单类型，波次号，批次,作业类型 ，库区  查询拣货的商品信息
	 * @param vo
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect>  queryOutstockDirectByArea(@Param("params")BillOmOutstockDirectForQuery vo)throws DaoException;
	
	/**
	 * 发单调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procOmOutStockDirect(Map<String, String> map) throws DaoException;
	
	/**
	 * 移库发单调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procOmPlanOutStockDirect(Map<String, String> map) throws DaoException;
	
	/**
	 * 查询移库发单分页总数
	 * @param authorityParams TODO
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public int selectOutstockDirectCount(@Param("params")BillOmOutstockDirect billOmOutstockDirect, @Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 查询移库发单分页
	 * @param page
	 * @param authorityParams TODO
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect> selectOutstockDirectByPage(@Param("page") SimplePage page,@Param("params")BillOmOutstockDirect billOmOutstockDirect, @Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params")BillOmOutstockDirect billOmOutstockDirect, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 移库发单储位列表界面只显示移库计划已审核未完结明细所在的库区分页汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectHmPlanCmDefareaCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 移库发单储位列表界面只显示移库计划已审核未完结明细所在的库区分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<CmDefarea> selectHmPlanCmDefareaByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询波次下的所有
	 * @param billOmOutstockDirect
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect> selectOutstockDirectExpNoGroupBy(@Param("params")BillOmOutstockDirect billOmOutstockDirect) throws DaoException;
	
	
	/**
     * 查询按客户波次分页总数
     * 
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int selectOutstockDirectByStoreCount(
    	@Param("params")BillOmOutstockDirectForQuery vo,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 查询按客户波次明细分页
     * 
     * @param page
     * @param orderByField
     * @param orderBy
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDirect> selectOutstockDirectByStoreByPage(
	    @Param("page") SimplePage page,
	    @Param("orderByField") String orderByField,
	    @Param("orderBy") String orderBy,
	    @Param("params")BillOmOutstockDirectForQuery vo,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;
    
    
    /**
     * 查询按客户波次明细合计
     * @param authorityParams TODO
     * @param map
     * @return
     * @throws DaoException
     */
    public SumUtilMap<String, Object> selectOutstockDirectByStoreSumQty(
    		@Param("params")BillOmOutstockDirectForQuery vo, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
    
    
    /**
     * 查询按库区波次分页总数
     * 
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int selectOutstockDirectByAreaCount(
    	@Param("params")BillOmOutstockDirectForQuery vo,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 查询按库区波次明细分页
     * 
     * @param page
     * @param orderByField
     * @param orderBy
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDirect> selectOutstockDirectByAreaByPage(
	    @Param("page") SimplePage page,
	    @Param("orderByField") String orderByField,
	    @Param("orderBy") String orderBy,
	    @Param("params")BillOmOutstockDirectForQuery vo,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;
    
    
    /**
     * 查询按库区波次明细合计
     * @param authorityParams TODO
     * @param map
     * @return
     * @throws DaoException
     */
    public SumUtilMap<String, Object> selectOutstockDirectByAreaSumQty(
    		@Param("params")BillOmOutstockDirectForQuery vo, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
    
    /**
     * 减去的预上预下数量 = 已调度数量 - 已拣货数量
     * @param vo
     * @return
     * @throws DaoException
     */
	public List<BillOmOutstockDirect> selectDispatchNoOutstockRealQty(@Param("params") BillOmLocate billOmLocate)throws DaoException;
	
	
    /**
     * 如果下架指示表存在状态为10的明细，则需要回滚预上和预下库存数；同时如果有实际拣货数量小于计划数量的拣货单明细，也需要减去对应的预上预下 
     * @param vo
     * @return
     * @throws DaoException
     */
	public List<BillOmOutstockDirect> selectNoOutstockQty(@Param("params") BillOmLocate billOmLocate)throws DaoException;
	
	/**
	 * 拣货波茨删除：管理客户锁定查询需要扣减预上的库存数量
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDirect> selectOutstockDirectSubtractContent(@Param("params") Map<String, Object> params) throws DaoException;
	
	/**
	 * 估计波茨删除下架指示信息
	 * @param outstockDirect
	 * @return
	 * @throws DaoException
	 */
	public int deleteOutstockDirectByLocateNo(BillOmOutstockDirect outstockDirect) throws DaoException;
	
}