package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AccContainerSku;
import com.yougou.logistics.city.service.AccContainerSkuService;
import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

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
@Service("accContainerSkuManager")
class AccContainerSkuManagerImpl extends BaseCrudManagerImpl implements AccContainerSkuManager {
    @Resource
    private AccContainerSkuService accContainerSkuService;

    @Override
    public BaseCrudService init() {
        return accContainerSkuService;
    }
    
    
    public int findPlateCount(Map<String,Object> params)throws ManagerException{
    	try {
			return accContainerSkuService.findPlateCount(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
    }
	
    public List<AccContainerSku> findPlateBypage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ManagerException{
    	try {
			return accContainerSkuService.findPlateBypage(page,orderByField,orderBy,params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
    }
    public List<AccContainerSku> selectPlateSub(@Param("params")Map<String,Object> params)throws ManagerException{
    	try {
			return accContainerSkuService.selectPlateSub(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
    }
}