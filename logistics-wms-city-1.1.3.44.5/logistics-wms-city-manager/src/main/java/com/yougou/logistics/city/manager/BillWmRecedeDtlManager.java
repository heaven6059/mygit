package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillWmRecedeDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillWmRecedeDtlDto;
import com.yougou.logistics.city.common.model.BillWmRecedeDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 退厂通知单
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
public interface BillWmRecedeDtlManager extends BaseCrudManager {
	
	public  <ModelType> boolean addWmRecedeDtl(String locno,String ownerNo,String recedeNo,Map<CommonOperatorEnum, List<ModelType>> params,String loginName,String loginCHName)throws ManagerException;
	
	public int selectCountMx(BillWmRecedeDtlDto dto,AuthorityParams authorityParams)throws ManagerException;
	
	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoByExpNo(BillWmRecedeDtlDto dto)throws ManagerException;

	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoGroupBy(SimplePage page,BillWmRecedeDtlDto dto,AuthorityParams authorityParams)throws ManagerException;

	/**
	 * 查询商品信息
	 * @param params
	 * @return
	 */
	public int selectItemCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	public int selectItemCountTest(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	public List<BillWmRecedeDtl> selectItemTest(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	public List<BillWmRecedeDtl> selectItem(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	public void excelImportData(List<BillWmRecedeDtl> list, String locno, String recedeNo, String ownerNo)throws ManagerException;
	
	/**
	 * 查询退厂调度明细
	 * @param params
	 * @return
	 */
	public int findWmRecedeDtlDispatchCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 查询退厂调度明细
	 * @param params
	 * @return
	 */
	public List<BillWmRecedeDispatchDtlDTO> findWmRecedeDtlDispatchByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;

	/**
	 * 退厂调度明细汇总
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
}