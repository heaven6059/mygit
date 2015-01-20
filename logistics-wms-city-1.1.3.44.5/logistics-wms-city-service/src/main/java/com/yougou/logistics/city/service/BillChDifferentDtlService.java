package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillChDifferentDtl;

public interface BillChDifferentDtlService extends BaseCrudService {
	/**
	 * 批量插入差异信息
	 * @param list
	 */
	public void batchInsertDtl(List<BillChDifferentDtl> list)throws ServiceException;
}