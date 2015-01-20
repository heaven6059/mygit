package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
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
public interface BillOmOutstockDtlManager extends BaseCrudManager {

    /**
     * 拣货单移库确认、回单
     * 
     * @param vo
     * @throws ManagerException
     */
    public void editDetail(List<BillOmOutstockDtl> oList, BillOmOutstock billOmOutstock, SystemUser user)
	    throws ManagerException;

    /**
     * 回单审核
     * 
     * @param locno
     * @param outstockNo
     * @param creator
     * @throws ServiceException
     */
    public void omPlanOutStockAudit(String outstockNo, SystemUser user) throws ManagerException;

    /**
     * 查询客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> findStoreNo(
	    @Param("dtl") BillOmOutstockDtl dtl) throws ManagerException;

    /**
     * 查询明细 有客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> findStockDtl(
	    @Param("dtl") BillOmOutstockDtl dtl) throws ManagerException;

    /**
     * 查询明细无客户
     * 
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillOmOutstockDtl> findStockDtlNoStoreNo(
	    @Param("dtl") BillOmOutstockDtl dtl) throws ManagerException;

    /**
     * 查询拣货单中需要复核的商品信息
     * 
     * @param params
     * @return
     */
    public List<BillOmOutstockDtlDto> findRecheckOutstockItem(
	    Map<String, Object> params) throws ManagerException;

    /**
     * 查询拣货单分页总数
     * 
     * @param params
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int findBillOmOutstockCount(Map<String, Object> params,
	    AuthorityParams authorityParams) throws ManagerException;

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
	    AuthorityParams authorityParams) throws ManagerException;

    /**
     * 根据参数查询客户信息
     * 
     * @param params
     *            outstockNo locno
     * @param authorityParams TODO
     * @return
     */
    public List<Store> findStoreByParam(Map<String, Object> params, AuthorityParams authorityParams)
	    throws ManagerException;

    /**
     * 拣货切单类型(库区拣货)打印
     * 
     * @param locno
     * @param keys
     * @param user
     * @return
     * @throws ManagerException
     */
    public Map<String, Object> printByArea(String locno, String keys,
	    SystemUser user) throws ManagerException;

    /**
     * 拣货切单类型(客户拣货)打印
     * 
     * @param locno
     * @param keys
     * @param user
     * @return
     * @throws ManagerException
     */
    public Map<String, Object> printByStore(String locno, String keys,
	    SystemUser user) throws ManagerException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

    public List<Map<String, Object>> getPrintInf4AreaCut(String locno,
	    String keystr, String curOper) throws ManagerException;
    
    
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
	    throws ManagerException;

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
	    throws ManagerException;
    
    public SumUtilMap<String, Object> selectRecheckOutstockItemSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

    /**
	 * 查询客户可移库分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findConContentGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException;

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
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 查询即时移库明细分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findMoveStockGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException;

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
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectMoveStockSumQty(
    	    @Param("params") Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	 /**
     * 即时移库
     * 
     * @param map
     * @throws DaoException
     */
    public void procImmediateMoveStock(List<BillOmOutstockDtlDto> lists,SystemUser user) throws ManagerException;
    
    /**
     * 转库存锁定
     * @param locno
     * @param outstockNo
     * @param creator
     * @throws ServiceException
     */
    //public void toStoreLock(String locno, String outstockNo,String creator) throws ManagerException;
    
    /**
     * 拣货回单按计划保存
     * @param instock
     * @return
     * @throws DaoException
     */
    public int saveByPlan(BillOmOutstock instock,SystemUser user) throws ManagerException;
    
    public int selectCheckDtlRealQty(BillOmOutstock instock) throws ManagerException;
    
    public int selectCheckDtlRealQtyEq(BillOmOutstock instock) throws ManagerException;
    
    /**
     * 尺码横排
     * @param map
     * @param authorityParams
     * @return
     * @throws ManagerException
     */
    public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;
    
    public List<String> selectSysNo(Map<String, String> map) throws ManagerException;
    
    public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
    
    public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	public List<BillOmOutstockDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ManagerException;
	
	public List<BillOmOutstockDtl> selectDetailBySizeNo(Map<String, Object> map) throws ManagerException;
}