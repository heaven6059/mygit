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
import com.yougou.logistics.city.common.model.SplitDepotDateSumReport;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.SplitDepotDateSumReportService;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-18 下午1:44:41
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("splitDepotDateSumReportManager")
public class SplitDepotDateSumReportManagerImpl extends BaseCrudManagerImpl implements SplitDepotDateSumReportManager {

	@Resource
	private SplitDepotDateSumReportService splitDepotDateSumReportService;
	
	@Override
	protected BaseCrudService init() {
		return splitDepotDateSumReportService;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return splitDepotDateSumReportService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<SplitDepotDateSumReport> findSplitDepotDateSumReportList(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return splitDepotDateSumReportService.findSplitDepotDateSumReportList(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
}
