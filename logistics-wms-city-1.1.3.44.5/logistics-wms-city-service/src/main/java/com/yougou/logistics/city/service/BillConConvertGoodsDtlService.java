package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtlSizeDto;
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
public interface BillConConvertGoodsDtlService extends BaseCrudService {

	/**
	 * 添加转货单明细
	 * @param goodsList
	 * @throws ServiceException
	 */
	public void saveConvertGoodsDtl(BillConConvertGoods convertGoods, List<BillUmCheck> insertList,
			List<BillUmCheck> deleteList) throws ServiceException;

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
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 库存转货明细汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findConvertGoodsDtlGroupByCheckCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;

	/**
	 * 查询所有的验收单groupby
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillConConvertGoodsDtl> findConvertGoodsDtlGroupByCheck(Map<String, Object> params)
			throws ServiceException;

	/**
	 * 验收单明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectGroupByCheckSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;

	public List<BillConConvertGoodsDtl> findItemDtlByParams(Map<String, Object> params) throws ServiceException;
	
	public List<BillConConvertGoodsDtl> findCheckContent4Convert (Map<String, Object> params) throws ServiceException;

	/**
	 *  明细打印尺码横排
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
	public List<BillConConvertGoodsDtlSizeDto> findDtl4SizeHorizontal(Map<String, Object> params, 
			AuthorityParams authorityParams)throws ServiceException;
	
	/**
	 * 更新箱状态为3,容器状态
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public Integer batchUpdateBoxStatus4Container(Map<String, Object> params)throws ServiceException;

}