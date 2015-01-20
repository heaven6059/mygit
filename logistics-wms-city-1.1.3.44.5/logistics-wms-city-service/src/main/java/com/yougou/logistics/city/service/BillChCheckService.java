package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.model.BillChCheck;
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
public interface BillChCheckService extends BaseCrudService {
	/**
	 *批量分配盘点人员
	 * @param checkNos
	 * @param assignName 
	 * @throws ServiceException
	 */
	public void distributionAssignNoBatch(String locno, String checkNos, String assignNo,int systemId,int areaSystemId) throws ServiceException;

	/**
	 * 生成盘点单
	 * @param dto
	 * @param check_date
	 * @param stockCount
	 * @param cellCount
	 * @param creatorName 
	 */
	public void createBillChCheck(BillChCheckDirectDto dto, String check_date, Integer stockCount, Integer cellCount,
			String creator, String creatorName) throws ServiceException;

	/**
	 * 出盘回单
	 * @param map
	 * @param page
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDto> selectChCheck(Map<String, Object> map, SimplePage page, AuthorityParams authorityParams) throws ServiceException;

	public void chCheck(String checkNos, String locno, String curUser,String curUsername) throws ServiceException;
	/**
	 * 获取itemNo为'N'且对应【记录1】的cellNo在所有明细里面存在itemNo不为'N'的【记录1】
	 * @param locno
	 * @param checkNo
	 * @return
	 */
	public void deleteDtl(String locno,String checkNo);
	/**
	 * 根据盘点计划单号，更新盘点单状态
	 * @param billChCheck
	 * @return
	 */
	public int updateStatusByPlanNo(BillChCheck billChCheck) throws ServiceException;
	
	public SumUtilMap<String, Object> selectChCheckSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 查找定位信息之外新增加的锁定储位编码
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public List<String> findCellNo4Add(Map<String, Object> map) throws ServiceException;
	
	public SumUtilMap<String, Object> selectChCheckPlanSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 查询盘点计划单下面的盘点单
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public List<String> selectChCheckContainerFlag(Map<String, Object> map) throws ServiceException;

}