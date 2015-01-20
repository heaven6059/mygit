package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;
import com.yougou.logistics.city.service.BillChCheckDtlService;

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
@Service("billChCheckDtlManager")
class BillChCheckDtlManagerImpl extends BaseCrudManagerImpl implements BillChCheckDtlManager {
	@Resource
	private BillChCheckDtlService billChCheckDtlService;

	@Override
	public BaseCrudService init() {
		return billChCheckDtlService;
	}

	@Override
	public List<BillChCheckDtl> selectCellNo(BillChCheck check) throws ManagerException {
		try {
			return billChCheckDtlService.selectCellNo(check);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public List<BillChCheckDtl> findCellNobyPlan(String planNo,String locNo) throws ManagerException {
		try {
			return billChCheckDtlService.findCellNobyPlan(planNo,locNo);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public ResultVo saveCheckDtl(List<BillChCheckDtl> insertList, List<BillChCheckDtl> updateList,
			List<BillChCheckDtl> deleteList, String checkNo, String locno, String checkWorker) throws ManagerException {
		try {
			return billChCheckDtlService.saveCheckDtl(insertList, updateList, deleteList, checkNo, locno);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(),e);
		}
	}

	@Override
	public List<BillChCheckDtl> selectItem4ChCheck(SimplePage page, BillChCheckDtl check,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChCheckDtlService.selectItem4ChCheck(page, check, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectItem4ChCheckCount(BillChCheckDtl check, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChCheckDtlService.selectItem4ChCheckCount(check, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public List<BillChCheckDtl> findItemByPlan(SimplePage page, BillChCheckDtl check,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChCheckDtlService.findItemByPlan(page, check, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public int findItemByPlanCount(BillChCheckDtl check, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChCheckDtlService.findItemByPlanCount(check, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectReChCheckCount(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChCheckDtlService.selectReChCheckCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillChCheckDto> selectReChCheck(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChCheckDtlService.selectReChCheck(map, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findCountByPlanNo(Map<String, Object> params) throws ManagerException {
		try {
			return billChCheckDtlService.findCountByPlanNo(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillChCheckDtl> findByPageByPlanNo(SimplePage page, Map<String, Object> params) throws ManagerException {
		try {
			return billChCheckDtlService.findByPageByPlanNo(page, params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectBrandLimitItemCount(Map<String, Object> params) throws ManagerException {
		try {
			return billChCheckDtlService.selectBrandLimitItemCount(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillChCheckDtl> selectBrandLimitItem(SimplePage page, Map<String, Object> params)
			throws ManagerException {
		try {
			return billChCheckDtlService.selectBrandLimitItem(page, params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public void saveByPlan(BillChCheck check) throws ManagerException {
		try {
			billChCheckDtlService.saveByPlan(check);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void resetPlan(BillChCheck check) throws ManagerException {
		try {
			billChCheckDtlService.resetPlan(check);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findByPage4CellCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billChCheckDtlService.findByPage4CellCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillChCheckDtl> findByPage4Cell(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChCheckDtlService.findByPage4Cell(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public List<Map<String, Object>> printBatch(String key) throws ManagerException {
		try {
			return billChCheckDtlService.printBatch(key);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	public SumUtilMap<String, Object> selectSumQty4Cell(Map<String, Object> map, AuthorityParams authorityParams) {
		return billChCheckDtlService.selectSumQty4Cell(map, authorityParams);
	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billChCheckDtlService.selectSumQty(map, authorityParams);
	}

	@Override
	public List<BillChCheckDtl> findDtlSysNo(BillChCheckDtl billChCheckDtl,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billChCheckDtlService.findDtlSysNo(billChCheckDtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> findDtlSysNoByPage(
			BillChCheckDtl billChCheckDtl, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billChCheckDtlService.findDtlSysNoByPage(billChCheckDtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}