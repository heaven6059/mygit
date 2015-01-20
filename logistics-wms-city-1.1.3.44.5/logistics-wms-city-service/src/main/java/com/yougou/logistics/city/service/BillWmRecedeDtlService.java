package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillWmRecedeDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillWmRecedeDtlDto;
import com.yougou.logistics.city.common.model.BillWmRecedeDtl;
import com.yougou.logistics.city.common.model.BillWmRecedeDtlKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
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
public interface BillWmRecedeDtlService extends BaseCrudService {
	
	public short selectMaxPid(BillWmRecedeDtlKey billWmRecedeDtlKey)throws ServiceException;
	
	public int selectCountMx(BillWmRecedeDtlDto dto,AuthorityParams authorityParams)throws ServiceException;
	
	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoByExpNo(BillWmRecedeDtlDto dto)throws ServiceException;

	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoGroupBy(SimplePage page,BillWmRecedeDtlDto dto,AuthorityParams authorityParams)throws ServiceException;
	
	public int selectItemCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillWmRecedeDtl> selectItem(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	
	public void excelImportData(List<BillWmRecedeDtl> list, String locno, String recedeNo, String ownerNo)throws ServiceException;
	
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	public int selectItemCountTest(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	public List<BillWmRecedeDtl> selectItemTest(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;

	/**
	 * 查询退厂调度明细
	 * @param params
	 * @return
	 */
	public int findWmRecedeDtlDispatchCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	/**
	 * 查询退厂调度明细
	 * @param params
	 * @return
	 */
	public List<BillWmRecedeDispatchDtlDTO> findWmRecedeDtlDispatchByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	
	/**
	 * 退厂调度明细汇总
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
}