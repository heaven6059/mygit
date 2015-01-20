package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.AccInventorySkuBookService;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-11 15:24:23
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
@Service("accInventorySkuBookManager")
class AccInventorySkuBookManagerImpl extends BaseCrudManagerImpl implements AccInventorySkuBookManager {
    @Resource
    private AccInventorySkuBookService accInventorySkuBookService;

    @Override
    public BaseCrudService init() {
        return accInventorySkuBookService;
    }

	
}