package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.dto.BillLmp2WmsDto;

public interface BillLmp2WmsService {

	/**
	 * 获取业务单据报表(分销-物流)所需的WMS数据
	 * @param billTypes
	 * @param startDate
	 * @param endDate
	 * @param sysNo
	 * @param locno
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<BillLmp2WmsDto> findBill4Wms(List<String> billTypes, String startDate,
			String endDate, String sysNo, String locno, SimplePage page) throws ServiceException;
}
