package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillSmOtherinDtlDto;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
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
public interface BillSmOtherinDtlManager extends BaseCrudManager {
	public int selectContentCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillSmOtherinDtlDto> selectContent(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
	public SumUtilMap<String, Object> findPageSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;

	public <ModelType> Map<String, Object> addSmOtherinDtl(BillSmOtherin billSmOtherin,
			Map<CommonOperatorEnum, List<ModelType>> params) throws ManagerException;
	/**
	 * 明细列表导入
	 * @param list
	 * @param locno
	 * @param otherinNo
	 * @param ownerNo
	 * @param authorityParams
	 * @throws ManagerException
	 */
	public void excelImportData(List<BillSmOtherinDtl> list, String locno, String otherinNo,String ownerNo,AuthorityParams authorityParams)throws ManagerException;
	
	public List<BillSmOtherinDtl> findDtlSysNo(BillSmOtherinDtl billSmOtherinDtl, AuthorityParams authorityParams) throws ManagerException;
	
	public Map<String,Object> findDtlSysNoByPage(BillSmOtherinDtl billSmOtherinDtl, AuthorityParams authorityParams)throws ManagerException;
}