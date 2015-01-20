package com.yougou.logistics.city.manager.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.api.BillStatusLogServiceApi;
import com.yougou.logistics.city.common.api.dto.BillStatusLogDto;
import com.yougou.logistics.city.service.BillStatusLogService;

/**
 * 状态日志Dubbo接口实现类
 * 
 * @author jiang.ys
 * @date 2013-12-11 下午3:30:55
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("billStatusLogServiceApi")
public class BillStatusLogServiceApiImpl implements BillStatusLogServiceApi{

	@Resource
    private BillStatusLogService billStatusLogService;
	@Override
	public Map<String, List<BillStatusLogDto>> getBillStatusLogByPoNo(List<String> poNoList) throws RpcException {
		if(poNoList == null || poNoList.size() == 0){
			return null;
		}else{
			try {
				Map<String, List<BillStatusLogDto>> map = new HashMap<String, List<BillStatusLogDto>>();
				for(String poNo : poNoList){
					map.put(poNo, billStatusLogService.findBillStatusLogByPoNo(poNo));
				}
				return map;
			} catch (ServiceException e) {
				e.printStackTrace();
				throw new RpcException("logistics-wms-city", e);
			}
		}
	}

	@Override
	public Map<String, List<BillStatusLogDto>> getBillStatusLogBySourceExpNo(List<String> sourceExpNoList)
			throws RpcException {
		if(sourceExpNoList == null || sourceExpNoList.size() == 0){
			return null;
		}else{
			try {
				Map<String, List<BillStatusLogDto>> map = new HashMap<String, List<BillStatusLogDto>>();
				for(String sourceExpNo : sourceExpNoList){
					map.put(sourceExpNo, billStatusLogService.findBillStatusLogBySourceExpNo(sourceExpNo));
				}
				return map;
			} catch (ServiceException e) {
				e.printStackTrace();
				throw new RpcException("logistics-wms-city", e);
			}
		}
	}

}
