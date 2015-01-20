package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillSmOtherinDtlDto;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.model.BillSmOtherinPrintDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
public interface BillSmOtherinDtlService extends BaseCrudService {
	/**
	 * 查询库存信息
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int selectContentCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillSmOtherinDtlDto> selectContent(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillSmOtherinDtl> selectContentParams(BillSmOtherinDtl modelType,Map<String,Object> params)throws ServiceException;
	public List<BillSmOtherinDtl> selectContentDtl(BillSmOtherinDtl modelType,Map<String,Object> params)throws ServiceException;
	public int updateContent(BillSmOtherinDtl modelType)throws ServiceException;
	/**
	 * 查询最大序列号
	 * @param billSmOtherinDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxPid(BillSmOtherinDtl billSmOtherinDtl) throws ServiceException;
	
	/**
	 * 查询是否有重复数据
	 * @param billSmOtherinDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectIsHave(BillSmOtherinDtl billSmOtherinDtl) throws ServiceException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
	
	public SumUtilMap<String, Object> selectPageSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 重复记录检查
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillSmOtherinDtl> findDuplicateRecord(Map<String, Object> params) throws ServiceException;
	
	/**
	 * 批量插入明细
	 * @param list
	 * @throws ServiceException
	 */
	public void batchInsertDtl(List<BillSmOtherinDtl> list) throws ServiceException;
	
	public List<BillSmOtherinDtl> findDtlSysNo(BillSmOtherinDtl billSmOtherinDtl, AuthorityParams authorityParams) throws ServiceException;
	
	public Map<String,Object> findDtlSysNoByPage(BillSmOtherinDtl billSmOtherinDtl, AuthorityParams authorityParams)throws ServiceException;
	
	public void updateRecheckQty4Convert(Map<String,Object> params)throws ServiceException;
	
	/**
	 * 查询打印尺码横排所需要的所有明细
	 * @param params
	 * @return
	 */
	public List<BillSmOtherinPrintDto> findPrintDtl4Size(Map<String,Object> params);
	
	public void updateOperateRecord(Map<String, Object> map) throws ServiceException;
}