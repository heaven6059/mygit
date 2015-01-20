package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.BillConAdjDtlSizeDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
public interface BillConAdjDtlService extends BaseCrudService {
	
	public List<BillConAdjDtl> selectItem(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	
	public int selectItemCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	
	public int checkAty(Map<String,Object> map)throws ServiceException;
	
	public int selectMaxRowId(BillConAdjDtl model)throws ServiceException;
	
	public int selectCellId(Map<String,Object> map)throws ServiceException;
	
	public List<BillConAdjDtl> selectContentParams(BillConAdjDtl modelType,Map<String,Object> params)throws ServiceException;	

	public List<String> findDtlCell(Map<String,Object> params)throws ServiceException;
	
	public List<String> findDtlCon(Map<String,Object> params)throws ServiceException;
	
	
	public List<String> findConIdFromDtl(Map<String,Object> params)throws ServiceException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 批量插入明细
	 * @param list
	 * @throws ServiceException
	 */
	public void batchInsertDtl(List<BillConAdjDtl> list) throws ServiceException;
	
	/**
	 * 批量插入明细
	 * @param list
	 * @throws ServiceException
	 */
	public void batchInsertDtl4ConvertGoods(List<BillConAdjDtl> list) throws ServiceException;

	/**
     * 库存调整打印明细
     * @param list
     * @throws ServiceException
     */
    public List<BillConAdjDtlSizeDto> findAllDtl(Map<String,Object> params, AuthorityParams authorityParams)throws ServiceException;

    public List<BillConAdjDtl> findCheckRepeatData(Map<String,Object> params) throws ServiceException;
    
    public List<BillConAdjDtl> selectListByConNo(Map<String,Object> params)throws ServiceException;
    
    public int deleteByConNo(Map<String, Object> tempQueryParam) throws ServiceException;

	public void updateOperateRecord(Map<String, Object> map) throws ServiceException;
}