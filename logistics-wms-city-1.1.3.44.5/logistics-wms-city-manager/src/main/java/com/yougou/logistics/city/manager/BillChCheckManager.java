package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 10:01:44 CST 2013
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
public interface BillChCheckManager extends BaseCrudManager {
	/**
	 * 分配盘点人员
	 * @param locno
	 * @param checkNos
	 * @param assignNo
	 * @param assignName 
	 * @throws ManagerException
	 */
	public void distributionAssignNoBatch(String locno, String checkNos, String assignNo,int systemId,int areaSystemId) throws ManagerException;

	/**
	 * 创建盘点单
	 * @param dto
	 * @param check_date
	 * @param stockCount
	 * @param cellCount
	 * @param creator
	 * @param creatorName 
	 * @throws ManagerException
	 */
	public void createBillChCheck(BillChCheckDirectDto dto, String check_date, Integer stockCount, Integer cellCount,
			String creator, String creatorName) throws ManagerException;

	public List<BillChCheckDto> selectChCheck(Map<String, Object> map, SimplePage page, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 初盘回单审核
	 * @param checkNos
	 * @param locno
	 * @param curUser TODO
	 * @throws ServiceException
	 */
	public void chCheck(String checkNos, String locno, String curUser,String curUsername) throws ManagerException;
	
	/**
	 * 
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws ManagerException
	 */
	public SumUtilMap<String, Object> selectChCheckSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 判断时候已经开始盘点(即盘点明细中check_qty是否有存在大于0的记录)
	 * @param locno
	 * @param checkNos 格式(checkNo_planNo|checkNo_planNo)
	 * @return
	 * @throws ManagerException
	 */
	public String hasBegunCheck(String locno,String checkNos) throws ManagerException;
	/**
	 * 还原盘点单
	 * @param locno
	 * @param values 格式(checkNo_planNo|checkNo_planNo)
	 * @return
	 * @throws ManagerException
	 */
	public String restoreCheck(String locno,String values) throws ManagerException;
	
	public SumUtilMap<String, Object> selectChCheckPlanSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
}