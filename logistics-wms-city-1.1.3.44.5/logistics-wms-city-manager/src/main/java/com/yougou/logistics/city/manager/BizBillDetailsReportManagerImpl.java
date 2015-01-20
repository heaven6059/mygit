package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BizBillDetailsReportService;

/**
 * 
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-6-23 上午10:34:13
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("bizBillDetailsReportManager")
public class BizBillDetailsReportManagerImpl extends BaseCrudManagerImpl implements BizBillDetailsReportManager {

	@Resource
	private BizBillDetailsReportService bizBillDetailsReportService;
	
	@Override
	protected BaseCrudService init() {
		return bizBillDetailsReportService;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return bizBillDetailsReportService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
}
