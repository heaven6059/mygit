package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConConvertDtl;
import com.yougou.logistics.city.common.model.BillConConvertDtlSizeDto;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
public interface BillConConvertDtlService extends BaseCrudService {
	
	public int findContentCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillConConvertDtl> findContentByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	/**
	 * 批量插入明细
	 * @param list
	 * @throws ServiceException
	 */
	public void batchInsertDtl(List<BillConConvertDtl> list) throws ServiceException;
	
	public Map<String, Object> findSumQty(Map<String, Object> params);
	
	/**
	 * 明细打印尺码横排
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
	public List<BillConConvertDtlSizeDto> findDtl4SizeHorizontal(Map<String, Object> params, AuthorityParams authorityParams)throws ServiceException;
}