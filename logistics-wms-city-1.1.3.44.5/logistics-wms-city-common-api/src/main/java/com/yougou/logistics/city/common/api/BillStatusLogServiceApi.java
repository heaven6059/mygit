package com.yougou.logistics.city.common.api;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.dto.BillStatusLogDto;

/**
 * 状态日志Dubbo接口
 * 
 * @author jiang.ys
 * @date 2013-12-11 下午2:48:22
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillStatusLogServiceApi {

	/**
	 * 根据预到货通知单分销合同号(s_po_no)获取状态日志信息
	 * @param spoNoList
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<BillStatusLogDto>> getBillStatusLogByPoNo(List<String> poNoList) throws RpcException;
	/**
	 * 根据发货通知单来源单号(sourceexp_no)获取状态日志信息
	 * @param sourceExpNoList
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<BillStatusLogDto>> getBillStatusLogBySourceExpNo(List<String> sourceExpNoList) throws RpcException;
}
