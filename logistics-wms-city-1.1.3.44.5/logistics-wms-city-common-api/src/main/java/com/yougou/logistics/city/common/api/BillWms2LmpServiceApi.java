package com.yougou.logistics.city.common.api;

import java.util.List;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.dto.Bill4WmsDto;
import com.yougou.logistics.city.common.api.dto.ItemManageQueryDto;
import com.yougou.logistics.city.common.api.dto.ItemManageVo;

/**
 * 业务单据核对 物流-分销
 * 
 * @author luo.hl
 * @date 2014-5-14 下午2:21:24
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillWms2LmpServiceApi {
	public List<Bill4WmsDto> getBill4Wms(String locno, String sysNo, List<String> billType, String beginDate,
			String endDate, SimplePage page) throws RpcException;

	/**
	 * LMP货管库存查询报表-分页查询
	 * @param queryDto
	 * @return
	 * @throws RpcException
	 */
	public ItemManageVo findItemManage(ItemManageQueryDto queryDto) throws RpcException;
}
