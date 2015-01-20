package com.yougou.logistics.city.common.api;

import java.util.List;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.dto.BillLmp2WmsDto;

/**
 * 分销到物流dubbo接口
 * 
 * @author jiang.ys
 * 
 */
public interface BillLmp2WmsServiceApi {

	/**
	 * 获取业务单据报表(分销-物流)所需的WMS数据
	 * @param billTypes
	 * @param startDate
	 * @param endDate
	 * @param sysNo
	 * @param locno
	 * @param page
	 * @return
	 * @throws RpcException
	 */
	public List<BillLmp2WmsDto> getBill4Wms(List<String> billTypes, String startDate,
			String endDate, String sysNo, String locno, SimplePage page) throws RpcException;
}
