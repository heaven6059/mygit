package com.yougou.logistics.city.manager.api;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.BillLmp2WmsServiceApi;
import com.yougou.logistics.city.common.api.dto.BillLmp2WmsDto;
import com.yougou.logistics.city.service.BillLmp2WmsService;

@Service("billLmp2WmsServiceApi")
public class BillLmp2WmsServiceApiImpl implements BillLmp2WmsServiceApi {

	@Resource
    private BillLmp2WmsService billLmp2WmsService;
	
	@Override
	public List<BillLmp2WmsDto> getBill4Wms(List<String> billTypes, String startDate,
			String endDate, String sysNo, String locno, SimplePage page) throws RpcException {
		if (startDate == null || endDate == null || StringUtils.isBlank(sysNo) || StringUtils.isBlank(locno)) {
			throw new RpcException("logistics-wms-city", "缺少参数!");
		}
		try {
			return billLmp2WmsService.findBill4Wms(billTypes, startDate, endDate, sysNo, locno, page);
		} catch (ServiceException e) {
			throw new RpcException("logistics-wms-city", e);
		}
	}

}
