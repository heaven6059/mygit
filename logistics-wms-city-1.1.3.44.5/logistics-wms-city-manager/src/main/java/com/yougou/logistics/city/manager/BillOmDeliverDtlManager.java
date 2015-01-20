package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlSizeDto;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 装车单详情manager
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:26:07
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverDtlManager extends BaseCrudManager {
	
	/**
	 * 添加装车明细
	 * @param locno
	 * @param deliverNo
	 * @param ownerNo
	 * @param params
	 * @param loginName
	 * @param transFlag
	 * @return
	 * @throws ManagerException
	 */
	public <ModelType> void addBillOmDeliverDtl(
			String locno, String deliverNo, String ownerNo, Map<CommonOperatorEnum, List<ModelType>> params, SystemUser user, String transFlag,AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 复核单明细查询
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int boxSelectCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillOmDeliverDtl> boxSelectQuery(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillOmDeliverDtl> boxDtlByParams(BillOmDeliverDtl modelType,Map<String,Object> params,AuthorityParams authorityParam)throws ManagerException;
	public int selectBoxCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillOmDeliverDtl> selectBoxStore(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 派车单明细查询
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int flagSelectCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillOmDeliverDtl> flagSelectQuery(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillOmDeliverDtl> flagDtlByParams(BillOmDeliverDtl modelType,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public int selectFlagCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillOmDeliverDtl> selectFlagStore(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 查询装车出库明细
	 * @param params
	 * @return
	 */
	public int findDeliverDtlBoxCount(Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException;
	public List<BillOmDeliverDtl> findDeliverDtlBoxByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException;
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
	
	public int findLoadproposeDeliverDtlBoxCount(Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException;
	public List<BillOmDeliverDtl> findLoadproposeDeliverDtlBoxByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException;
	
	public List<BillOmDeliverDtlSizeDto> findDtl4SizeHorizontal(String deliverNo);
}