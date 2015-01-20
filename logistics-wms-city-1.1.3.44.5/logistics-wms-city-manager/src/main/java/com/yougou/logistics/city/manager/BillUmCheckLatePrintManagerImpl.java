package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmCheckLatePrint;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmCheckLatePrintService;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-8-14 下午3:10:07
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("billUmCheckLatePrintManager")
public class BillUmCheckLatePrintManagerImpl extends BaseCrudManagerImpl implements BillUmCheckLatePrintManager {

	@Resource
	private BillUmCheckLatePrintService billUmCheckLatePrintService;

	@Override
	protected BaseCrudService init() {
		return billUmCheckLatePrintService;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckLatePrintService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillUmCheckLatePrint> findItemInfoByBarcode(Map<String, Object> params, List<String> list)
			throws ManagerException {
		try {
			return billUmCheckLatePrintService.findItemInfoByBarcode(params, list);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

}
