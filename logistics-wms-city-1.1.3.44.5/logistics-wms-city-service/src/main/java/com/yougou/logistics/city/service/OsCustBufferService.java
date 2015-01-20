package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmLabelFullPrint;
import com.yougou.logistics.city.common.model.OsCustBuffer;
import com.yougou.logistics.city.common.model.Store;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-11-26 14:47:41
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface OsCustBufferService extends BaseCrudService {

	/**
	 * 批量新增客户暂存区维护
	 * @param custBuffer
	 * @param list
	 * @return
	 * @throws DaoException
	 */
	public void insertBatch(OsCustBuffer custBuffer, List<Store> storeList) throws ServiceException;
	/**
	 * 根据品牌库查询线路(暂存区名称)
	 * @param params
	 * @return
	 */
	public List<BillUmLabelFullPrint> findBufferBySys(Map<String, Object> params);
	/**
	 * 获取完整打印店铺信息的总数量
	 * @param params
	 * @return
	 */
	public int findFullPrintCount(Map<String,Object> params);
	/**
	 * 获取完整打印店铺信息
	 * @param page
	 * @param params
	 * @return
	 */
	public List<BillUmLabelFullPrint> findFullPrintByPage(SimplePage page,Map<String,Object> params);
}