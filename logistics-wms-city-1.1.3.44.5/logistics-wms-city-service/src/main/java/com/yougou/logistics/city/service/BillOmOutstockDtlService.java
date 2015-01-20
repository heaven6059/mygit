package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
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
public interface BillOmOutstockDtlService extends BaseCrudService {

    /**
     * 移库回单、确认
     * 
     * @param dtl
     * @throws ServiceException
     */
    public void editDetail(List<BillOmOutstockDtl> oList,BillOmOutstock billOmOutstock, SystemUser user)
	    throws ServiceException;

    /**
     * 移库审核
     * 
     * @param locno
     *            TODO
     * @param outstockNo
     * @param creator
     * @throws ServiceException
     */
    public void omPlanOutStockAudit(String outstockNo, SystemUser user) throws ServiceException;

    /**
     * 查询客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> findStoreNo(
	    @Param("dtl") BillOmOutstockDtl dtl) throws ServiceException;

    /**
     * 查询明细 有客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> findStockDtl(
	    @Param("dtl") BillOmOutstockDtl dtl) throws ServiceException;

    /**
     * 查询明细无客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> findStockDtlNoStoreNo(
	    @Param("dtl") BillOmOutstockDtl dtl) throws ServiceException;

    /**
     * 查询拣货单中需要复核的商品信息
     * 
     * @param params
     * @return
     */
    public List<BillOmOutstockDtlDto> findRecheckOutstockItem(
	    Map<String, Object> params) throws ServiceException;

    /**
     * 查询拣货单分页总数
     * 
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int findBillOmOutstockCount(Map<String, Object> params,
	    AuthorityParams authorityParams) throws ServiceException;

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
    public List<BillOmOutstockDtl> findBillOmOutstockByPage(SimplePage page,
	    String orderByField, String orderBy, Map<String, Object> params,
	    AuthorityParams authorityParams) throws ServiceException;

    /**
     * 根据参数查询客户信息
     * 
     * @param params
     *            outstockNo locno
     * @param authorityParams TODO
     * @return
     */
    public List<Store> findStoreByParam(Map<String, Object> params, AuthorityParams authorityParams)
	    throws ServiceException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

    public List<Map<String, Object>> getPrintInf4AreaCut(String locno,
	    String keys, String curOper) throws ServiceException;
    
    /**
     * 查询复核的拣货单明细分页总数
     * 
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int findRecheckOutstockItemCount(
	    Map<String, Object> params,
	    AuthorityParams authorityParams)
	    throws ServiceException;

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
    public List<BillOmOutstockDtlDto> findRecheckOutstockItemByPage(
	    SimplePage page,
	    String orderByField,
	    String orderBy,
	    Map<String, Object> params,
	    AuthorityParams authorityParams)
	    throws ServiceException;
    
    
	public SumUtilMap<String, Object> selectRecheckOutstockItemSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
	
	
	/**
	 * 查询客户可移库分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findConContentGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询客户可移库分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDtlDto> findConContentGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	
	/**
	 * 查询即时移库明细分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findMoveStockGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询即时移库明细分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstockDtlDto> findMoveStockGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	
	public SumUtilMap<String, Object> selectMoveStockSumQty(
    	    @Param("params") Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
	
	
	 /**
     * 即时移库
     * 
     * @param map
     * @throws DaoException
     */
    public void procImmediateMoveStock(List<BillOmOutstockDtlDto> lists,SystemUser user) throws ServiceException;
    
    /**
     * 转库存锁定
     * @param locno
     * @param outstockNo
     * @param creator
     * @throws ServiceException
     */
    //public void toStoreLock(String locno, String outstockNo,String creator) throws ServiceException;
    
    /**
     * 查看移库回单是否被RH操作
     * @param locno
     * @param outstockNo
     * @return
     * @throws ServiceException
     */
    public boolean isOperateByRF(String locno,String outstockNo) throws ServiceException;
    
    /**
     * 拣货回单按计划保存
     * @param instock
     * @return
     * @throws DaoException
     */
    public int saveByPlan(BillOmOutstock instock,SystemUser user) throws ServiceException;
    
    public int selectCheckDtlRealQty(BillOmOutstock instock) throws ServiceException;
    
    public int selectCheckDtlRealQtyEq(BillOmOutstock instock) throws ServiceException;
    
    /**
     * 验证RF数量和实际数量是否正确
     * @param instock
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> selectOutstockDtlCheckoedQty(BillOmOutstock instock) throws ServiceException;
    
    public List<String> selectSysNo(Map<String, String> map) throws ServiceException;

	public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException;
	
	public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
	
	public List<BillOmOutstockDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ServiceException;

	public List<BillOmOutstockDtl> selectDetailBySizeNo(Map<String, Object> map) throws ServiceException;
	
	public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException;

	public int selectCheckAssignName(BillOmOutstock instock) throws ServiceException;
	
	/**
	 * 如果实际拣货人为空,更新指定拣货人为实际拣货人
	 * @param instock
	 * @throws DaoException
	 */
	public void updateOutstockName4AssignName(BillOmOutstock instock) throws ServiceException;
}