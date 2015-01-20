package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
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
public interface BillChCheckDtlService extends BaseCrudService {

	public List<BillChCheckDtl> selectCellNo(@Param("check") BillChCheck check) throws ServiceException;	

	/**
	 * 查询盘点计划明细中的储位
	 * @param planNo
	 * @param locNo
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDtl> findCellNobyPlan(String planNo,String locNo) throws ServiceException;

	public ResultVo saveCheckDtl(List<BillChCheckDtl> insertList, List<BillChCheckDtl> updateList,
			List<BillChCheckDtl> deleteList, String checkNo, String locno) throws ServiceException;

	/**
	 * 选择商品
	 * 
	 * @param page
	 * @param check
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDtl> selectItem4ChCheck(SimplePage page, BillChCheckDtl check, AuthorityParams authorityParams) throws ServiceException;

	public int selectItem4ChCheckCount(BillChCheckDtl check, AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 查找盘点计划单内的商品
	 * @param page
	 * @param check
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDtl> findItemByPlan(SimplePage page, BillChCheckDtl check, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查找盘点计划单内的商品总数
	 * @param check
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
	public int findItemByPlanCount(BillChCheckDtl check, AuthorityParams authorityParams) throws ServiceException;
	/**
	 * 复盘发单
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public int selectReChCheckCount(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

	public List<BillChCheckDto> selectReChCheck(Map<String, Object> map, SimplePage page, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 根据盘点计划单查询盘点明细数量
	 * 
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int findCountByPlanNo(@Param("params") Map<String, Object> params) throws ServiceException;

	/**
	 * 根据盘点计划单查询盘点明细
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDtl> findByPageByPlanNo(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> params) throws ServiceException;

	/**
	 * 查询商品明细总数（商品盘、限制品牌）
	 * 
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int selectBrandLimitItemCount(Map<String, Object> params) throws ServiceException;

	/**
	 * 查询商品明细（商品盘、限制品牌）
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDtl> selectBrandLimitItem(SimplePage page, Map<String, Object> params)
			throws ServiceException;

	public void saveByPlan(@Param("item") BillChCheck check) throws ServiceException;

	public void resetPlan(@Param("item") BillChCheck check) throws ServiceException;

	/**
	 * 查询存在盘点差异的盘点储位总数
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public int findByPage4CellCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询存在盘点差异的盘点储位
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDtl> findByPage4Cell(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 批量打印
	 * @param key
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> printBatch(String key) throws ServiceException;
	
	public SumUtilMap<String, Object> selectSumQty4Cell(Map<String, Object> map, AuthorityParams authorityParams);

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);
	/**
	 * 撤销盘点单时将储位盘点状态置为可用
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int modityCellCheckStatusByCheckDtl(Map<String, Object> params) throws ServiceException;
	
	public int selectCountSingFlag(Map<String, Object> map) throws ServiceException;
	
	public List<BillChCheckDtl> findDtlSysNo(BillChCheckDtl billChCheckDtl,
			AuthorityParams authorityParams) throws ServiceException;

	public Map<String, Object> findDtlSysNoByPage(
			BillChCheckDtl billChCheckDtl, AuthorityParams authorityParams)throws ServiceException;
}
