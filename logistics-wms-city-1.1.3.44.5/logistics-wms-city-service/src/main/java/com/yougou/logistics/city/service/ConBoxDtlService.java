package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 21:07:33
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
public interface ConBoxDtlService extends BaseCrudService {

	public List<ConBoxDtl> findByParam(Map<String, Object> queryParam) throws ServiceException;

	public int countBoxAndNum(ConBoxDtl cc,AuthorityParams authorityParams) throws ServiceException;

	public List<ConBoxDtl> findCnBoxAndNumPage(SimplePage page, ConBoxDtl cc,AuthorityParams authorityParams) throws ServiceException;

	public int selectItem4umuntreadCount(Map<String, String> map, AuthorityParams authorityParams) throws ServiceException;

	public List<ConBoxDtl> selectItem4umuntread(Map<String, String> map, SimplePage page, AuthorityParams authorityParams) throws ServiceException;

	public ConBoxDtl selectBoxDtl4Recheck(Map<String, Object> params) throws ServiceException;
	
	public Integer selectMaxBoxId(ConBoxDtl boxDtl) throws ServiceException;
	
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
}