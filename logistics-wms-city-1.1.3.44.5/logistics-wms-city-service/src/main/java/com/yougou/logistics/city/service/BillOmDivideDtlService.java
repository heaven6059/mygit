package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillOmDivideDtlService extends BaseCrudService {
	
	/**
	 * 更新分货人员
	 * @param divideDtl
	 * @return
	 */
	public int modifyBillOmDivideByDivideNoAndlocno(BillOmDivide divide) throws ServiceException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询分货明细未复核的箱号
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDtl> findDivideDtlNotRecheckBox(Map<String, Object> params) throws ServiceException;
	
	public List<BillOmDivideDtl> findDivideDtl4Different(Map<String, Object> params) throws ServiceException;

	public int updateDivideDtl4Different(BillOmDivideDtl divideDtl) throws ServiceException;
	
	/**
	 * 根据差异调整单查询分货明细数据
	 * @param divideDtl
	 * @return
	 * @throws DaoException
	 */
	public BillOmDivideDtl selectDivideDtlByDifferent(BillOmDivideDtl divideDtl) throws ServiceException;
	
	/**
	 * 获取DIVIDEID
	 * @return
	 * @throws DaoException
	 */
	public Long selectDivideId() throws ServiceException;
}