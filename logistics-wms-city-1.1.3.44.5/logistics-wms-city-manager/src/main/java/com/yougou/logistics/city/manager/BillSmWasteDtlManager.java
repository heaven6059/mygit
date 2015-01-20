package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.BillSmWasteDtlSizeDto;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
public interface BillSmWasteDtlManager extends BaseCrudManager {
	
	public int selectContentCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillSmWasteDtl> selectContent(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	public <ModelType> Map<String, Object> addSmWasteDtl(BillSmWaste billSmWaste,
			Map<CommonOperatorEnum, List<ModelType>> params,AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException;
	
	public String importDtl(List<BillSmWasteDtl> list,String wasteNo,String ownerNo,SystemUser user) throws ManagerException;

	public <ModelType> Map<String, Object> addShipmentDtl(BillSmWaste billSmWaste,
			Map<CommonOperatorEnum, List<ModelType>> params, AuthorityParams authorityParams) throws ManagerException;
		
	public List<BillSmWasteDtlSizeDto> findDtl4SizeHorizontal(String wasteNo);
	
	public List<BillSmWasteDtl> findDtlSysNo(BillSmWasteDtl billSmWasteDtl, AuthorityParams authorityParams) throws ManagerException;
	
	public Map<String,Object> findDtlSysNoByPage(BillSmWasteDtl billSmWasteDtl, AuthorityParams authorityParams)throws ManagerException;
	
	public void saveWasteBoxContainer(List<BillSmWasteDtl> boxList,String loginName,String userName) throws ManagerException;
}