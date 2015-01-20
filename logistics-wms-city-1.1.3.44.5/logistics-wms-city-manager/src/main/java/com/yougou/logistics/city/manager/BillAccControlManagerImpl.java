package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillAccControl;
import com.yougou.logistics.city.service.BillAccControlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Fri Mar 07 16:10:55 CST 2014
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
@Service("billAccControlManager")
class BillAccControlManagerImpl extends BaseCrudManagerImpl implements BillAccControlManager {
	
    @Resource
    private BillAccControlService billAccControlService;

    @Override
    public BaseCrudService init() {
        return billAccControlService;
    }

	@Override
	public List<BillAccControl> findBillAccControlGroupByBillName(Map<String, Object> maps) throws ManagerException {
		try{
			return billAccControlService.findBillAccControlGroupByBillName(maps);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}