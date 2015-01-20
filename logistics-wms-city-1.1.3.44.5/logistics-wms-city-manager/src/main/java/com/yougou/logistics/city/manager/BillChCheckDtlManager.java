package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;

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
public interface BillChCheckDtlManager extends BaseCrudManager {

	public List<BillChCheckDtl> selectCellNo(BillChCheck check) throws ManagerException;
	
	/**
	 * 查询盘点计划明细中的储位
	 * @param planNo
	 * @param locNo
	 * @return
	 * @throws ManagerException
	 */
	public List<BillChCheckDtl> findCellNobyPlan(String planNo,String locNo) throws ManagerException;

	public List<BillChCheckDtl> selectItem4ChCheck(SimplePage page, BillChCheckDtl check, AuthorityParams authorityParams) throws ManagerException;

	public int selectItem4ChCheckCount(BillChCheckDtl dto, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 查找盘点计划单内的商品
	 * @param page
	 * @param check
	 * @param authorityParams
	 * @return
	 * @throws ManagerException
	 */
	public List<BillChCheckDtl> findItemByPlan(SimplePage page, BillChCheckDtl check, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 查找盘点计划单内的商品总数
	 * @param dto
	 * @param authorityParams
	 * @return
	 * @throws ManagerException
	 */
	public int findItemByPlanCount(BillChCheckDtl dto, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 保存出盘回单明细
	 * 
	 * @param updateList
	 *            TODO
	 * @param deleteList
	 *            TODO
	 * @param checkWorker
	 *            TODO
	 * @param dtl
	 * @throws ManagerException
	 */
	public ResultVo saveCheckDtl(List<BillChCheckDtl> insertList, List<BillChCheckDtl> updateList,
			List<BillChCheckDtl> deleteList, String checkNo, String locno, String checkWorker) throws ManagerException;

	/**
	 * 复盘发单
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws ManagerException
	 */
	public int selectReChCheckCount(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

	public List<BillChCheckDto> selectReChCheck(Map<String, Object> map, SimplePage page, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 根据盘点计划单查询盘点明细数量
	 * 
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int findCountByPlanNo(@Param("params") Map<String, Object> params) throws ManagerException;

	/**
	 * 根据盘点计划单查询盘点明细
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<BillChCheckDtl> findByPageByPlanNo(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> params) throws ManagerException;

	/**
	 * 查询商品明细总数（商品盘、限制品牌）
	 * 
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int selectBrandLimitItemCount(Map<String, Object> params) throws ManagerException;

	/**
	 * 查询商品明细（商品盘、限制品牌）
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<BillChCheckDtl> selectBrandLimitItem(SimplePage page, Map<String, Object> params)
			throws ManagerException;

	public void saveByPlan(BillChCheck check) throws ManagerException;

	public void resetPlan(BillChCheck check) throws ManagerException;

	/**
	 * 查询存在盘点差异的盘点储位总数
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ManagerException
	 */
	public int findByPage4CellCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询存在盘点差异的盘点储位
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ManagerException
	 */
	public List<BillChCheckDtl> findByPage4Cell(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 批量打印
	 * @param key
	 * @return
	 * @throws ManagerException
	 */
	public List<Map<String, Object>> printBatch(String key) throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty4Cell(Map<String, Object> map, AuthorityParams authorityParams);

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

	public List<BillChCheckDtl> findDtlSysNo(BillChCheckDtl billChCheckDtl,
			AuthorityParams authorityParams) throws ManagerException;

	public Map<String, Object> findDtlSysNoByPage(
			BillChCheckDtl billChCheckDtl, AuthorityParams authorityParams) throws ManagerException;
}