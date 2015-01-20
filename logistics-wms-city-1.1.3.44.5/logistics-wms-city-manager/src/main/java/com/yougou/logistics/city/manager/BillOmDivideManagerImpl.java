package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.vo.ResultVo;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.service.BillOmDivideBoxService;
import com.yougou.logistics.city.service.BillOmDivideService;

/**
 * 
 * TODO: 分货任务单manager实现类
 * 
 * @author su.yq
 * @date 2013-10-14 下午6:17:30
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmDivideManager")
class BillOmDivideManagerImpl extends BaseCrudManagerImpl implements BillOmDivideManager {
	@Resource
	private BillOmDivideService billOmDivideService;
	
	@Resource
	private BillOmDivideBoxService billOmDivideBoxService;

	@Override
	public BaseCrudService init() {
		return billOmDivideService;
	}

	@Override
	public Map<String, Object> addBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ManagerException {
		try {
			return this.billOmDivideService.addBillOmDivide(listBillOmDivide);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, Object> addBillOmDivideNew(List<BillOmDivide> listBillOmDivide) throws ManagerException {
		try {
			return this.billOmDivideBoxService.addBillOmDivide(listBillOmDivide);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}


	@Override
	public void deleteBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ManagerException {
		try {
			billOmDivideService.deleteBillOmDivide(listBillOmDivide);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public ResultVo modifyCompleteBillOmDivide(BillOmDivide divide) throws ManagerException {
		try {
			return billOmDivideService.modifyCompleteBillOmDivide(divide);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void procOmDivideOver(BillOmDivide billOmDivide) throws ManagerException {
		try {
			billOmDivideService.procOmDivideOver(billOmDivide);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	public List<Map<String, Object>> getBatchPrintInfo(List<BillOmDivide> listOmDivides) throws ManagerException {
		try {
			return billOmDivideService.getBatchPrintInfo(listOmDivides);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}