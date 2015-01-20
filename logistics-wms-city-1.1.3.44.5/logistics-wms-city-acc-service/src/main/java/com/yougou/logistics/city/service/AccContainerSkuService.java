package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AccContainerSku;
import com.yougou.logistics.city.common.vo.AccInventorySkuBookVo;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-08-08 13:49:01
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
public interface AccContainerSkuService extends BaseCrudService {
	
	/**
	 * 保存更新容器商品明细实时账
	 *  @param skuBookVo
	 * @return
	 * @throws ServiceException
	 */
	public void saveOrUpdateAccContainerSku(AccInventorySkuBookVo skuBookVo) throws ServiceException;
	
	/**
	 * 查询板库存明细
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	int findPlateCount(Map<String,Object> params)throws ServiceException;
	List<AccContainerSku> findPlateBypage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ServiceException;
	List<AccContainerSku>  selectPlateSub(Map<String,Object> params)throws ServiceException;
}