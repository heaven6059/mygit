package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDivide;

/**
 * 
 * TODO: 分货任务单Service接口
 * 
 * @author hou.hm
 * @date 2014-08-06 下午7:52:44
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDivideBoxService extends BaseCrudService {

	/**
	 * 创建分货任务单
	 * @param divide
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> addBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ServiceException;
	
}