package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefware;

/**
 * 
 * 库区service
 * 
 * @author qin.dy
 * @date 2013-9-26 上午10:09:49
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefareaService extends BaseCrudService {
	
	public abstract List<?> findByWareAndArea(@Param("map") Map<String, Object> map) throws ServiceException;
	
	/**
	 * 验证查询仓区是否存在库区
	 * @param listLocateRuleDtls
	 * @return
	 */
	public List<CmDefarea> findCmDefareaIsHaveByWareNo(Map<String,Object> params,List<CmDefware> listCmDefwares) throws ServiceException;

	public abstract List<CmDefarea> findByStoreroom(Map<String, Object> params);
}