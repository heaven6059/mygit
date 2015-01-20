package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.AccContainerSku;

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
public interface AccContainerSkuManager extends BaseCrudManager {
	/**
	 * 查询板库存明细
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	int findPlateCount(Map<String,Object> params)throws ManagerException;
	List<AccContainerSku> findPlateBypage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ManagerException;
	List<AccContainerSku> selectPlateSub(@Param("params")Map<String,Object> params)throws ManagerException;
}