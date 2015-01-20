package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtlDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Dec 17 18:31:03 CST 2013
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
public interface BillChRecheckDtlService extends BaseCrudService {
	/**
	 * 创建复盘盘点单
	 * 
	 * @param dtllist
	 * @param recheckWorker
	 * @param areaSystemId 
	 * @param systemId 
	 * @throws ServiceException
	 */
	public void createChReCheckDtl(List<BillChCheckDtl> dtllist, String recheckWorker, int systemId, int areaSystemId) throws ServiceException;

	/**
	 * 汇总明细
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public int selectReCheckCount(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

	public List<BillChRecheckDtlDto> selectReCheck(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询复盘所有储位
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public List<BillChRecheckDtl> selectCellNo(BillChRecheckDtl check) throws ServiceException;

	public ResultVo updateReCheckDtl(List<BillChRecheckDtl> insertList, List<BillChRecheckDtl> updateList,
			List<BillChRecheckDtl> deleteList, String recheckNo, String checkNo, String locno) throws ServiceException;

	public void chReCheckAudit(String dtls, String locno) throws ServiceException;

	public void saveByPlan(BillChRecheckDtl check) throws ServiceException;

	public void resetPlan(BillChRecheckDtl check) throws ServiceException;

	/**
	 * 根据盘点计划单号，更新复盘单状态
	 * @param billChCheck
	 * @return
	 */
	public int updateStatusByPlanNo(Map<String, Object> map) throws ServiceException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);
	

	public SumUtilMap<String, Object> selectChReCheckSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
}