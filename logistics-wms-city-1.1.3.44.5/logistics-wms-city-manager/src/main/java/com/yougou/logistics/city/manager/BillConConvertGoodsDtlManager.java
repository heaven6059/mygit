package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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
public interface BillConConvertGoodsDtlManager extends BaseCrudManager {
	
	/**
	 * 添加转货单明细
	 * @param goodsList
	 * @throws ServiceException
	 */
	public void saveConvertGoodsDtl(BillConConvertGoods convertGoods, List<BillUmCheck> insertList,
			List<BillUmCheck> deleteList) throws ManagerException;
	
	/**
	 * 库存转货明细单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillConConvertGoodsDtl> findConvertGoodsDtlGroupByCheckByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 库存转货明细汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findConvertGoodsDtlGroupByCheckCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 查询所有的验收单groupby
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillConConvertGoodsDtl> findConvertGoodsDtlGroupByCheck(Map<String, Object> params)
			throws ManagerException;

	/**
	 * 验收单明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectGroupByCheckSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 明细打印尺码横排
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<Map<String, Object>> findDtl4SizeHorizontal(
			String keys, AuthorityParams authorityParams)throws ManagerException;
}