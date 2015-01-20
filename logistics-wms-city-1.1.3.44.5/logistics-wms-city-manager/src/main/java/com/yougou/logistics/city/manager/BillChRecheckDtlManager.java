package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BillChRecheckDtlManager extends BaseCrudManager {
	public void createChReCheckDtl(List<BillChCheckDtl> dtllist, String recheckWorker, int systemId, int areaSystemId) throws ManagerException;

	public int selectReCheckCount(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

	public List<BillChRecheckDtlDto> selectReCheck(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException;

	public List<BillChRecheckDtl> selectCellNo(BillChRecheckDtl check) throws ManagerException;

	public ResultVo updateReCheckDtl(List<BillChRecheckDtl> insertList, List<BillChRecheckDtl> updateList,
			List<BillChRecheckDtl> deleteList, String recheckNo, String checkNo, String locno) throws ManagerException;

	/**
	 * 审核
	 * 
	 * @param dtls
	 * @param locno
	 * @throws ManagerException
	 */
	public void chReCheckAudit(String dtls, String locno) throws ManagerException;

	public void saveByPlan(BillChRecheckDtl check) throws ManagerException;

	public void resetPlan(BillChRecheckDtl check) throws ManagerException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);
	
	public SumUtilMap<String, Object> selectChReCheckSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
}