package com.yougou.logistics.city.manager;

import java.util.List;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmOutstock;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;
import com.yougou.logistics.city.service.BillWmOutstockDirectService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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
@Service("billWmOutstockDirectManager")
class BillWmOutstockDirectManagerImpl extends BaseCrudManagerImpl implements BillWmOutstockDirectManager {
	
    @Resource
    private BillWmOutstockDirectService billWmOutstockDirectService;

    @Override
    public BaseCrudService init() {
        return billWmOutstockDirectService;
    }

	@Override
	public void sendWmOutstockDirect(BillWmOutstock billWmOutstock,List<BillWmOutstockDirect> listDirects) throws ManagerException {
		try {
			billWmOutstockDirectService.sendWmOutstockDirect(billWmOutstock,listDirects);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
    
}