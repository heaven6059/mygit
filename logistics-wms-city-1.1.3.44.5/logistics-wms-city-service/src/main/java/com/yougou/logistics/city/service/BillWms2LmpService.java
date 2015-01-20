package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.dto.Bill4WmsDto;
import com.yougou.logistics.city.common.api.dto.ItemManageResultDto;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-5-14 下午2:42:29
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillWms2LmpService {
	List<Bill4WmsDto> getBill4Wms(String locno, String sysNo, List<String> billType, String beginDate,
			String endDate, SimplePage page) throws ServiceException;
	
	/**
	 * LMP货管库存查询报表-查询总数据条数
	 * @param params
	 * @return
	 */
	public int findItemManageContentCount(Map<String, Object> params) throws ServiceException;
	/**
	 * LMP货管库存查询报表-分页查询
	 * @param map
	 * @param page
	 * @return
	 */
	public List<ItemManageResultDto> findItemManageContentByPage(Map<String, Object> params, SimplePage page) throws ServiceException;
	/**
	 * LMP货管库存查询报表-合计
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int findItemManageContentNum(Map<String, Object> params) throws ServiceException;
}
