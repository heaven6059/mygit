package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 14:54:50 CST 2013
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
public interface BillChCheckDirectManager extends BaseCrudManager {
	/**
	 * 出盘发单查询定位表信息数量
	 * @param dto
	 * @return
	 */
	public int findDirectCount(BillChCheckDirectDto dto) throws ManagerException;

	/**
	 * 出盘发单查询定位表信息
	 * @param dto
	 * @param orderBy TODO
	 * @return
	 */
	public List<BillChCheckDirectDto> findDirectList(BillChCheckDirectDto dto, String orderBy, SimplePage page) throws ManagerException;

	/**
	 * 查询定位表中存在的商品的品牌
	 * @param dto
	 * @return
	 * @throws ManagerException
	 */
	public List<Brand> findBrandInDirect(BillChCheckDirectDto dto) throws ManagerException;

	/**
	 * 根据盘点计划单生成定位单
	 * @param billChPlan
	 * @param authorityParams TODO
	 * @return
	 * @throws ManagerException
	 */
	public String createDirect(BillChPlan billChPlan,SystemUser user, AuthorityParams authorityParams) throws ManagerException;
	/**
	 * 取消盘点计划单下的部分定位单
	 * @param paramStr
	 * @param locno
	 * @param planNo
	 * @param startStatus
	 * @param targetStatus
	 * @return
	 * @throws ManagerException
	 */
	public String changeDirect(String paramStr,String locno,String planNo,String startStatus,String targetStatus,SystemUser user) throws ManagerException;

	public List<BillChCheckDirect> selectPlanNo(Map<String, String> map, SimplePage page) throws ManagerException;

	public int selectPlanNoCount(Map<String, String> map) throws ManagerException;
	
	public Map<String,Object> selectAllCellCountAndStockCount(BillChCheckDirectDto dto)throws ManagerException;
}