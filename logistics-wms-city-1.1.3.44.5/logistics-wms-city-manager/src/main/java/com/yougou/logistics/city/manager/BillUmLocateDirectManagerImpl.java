package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BillUmLocateDirectService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Nov 13 17:35:14 CST 2013
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
@Service("billUmLocateDirectManager")
class BillUmLocateDirectManagerImpl extends BaseCrudManagerImpl implements BillUmLocateDirectManager {
    @Resource
    private BillUmLocateDirectService billUmLocateDirectService;

    @Override
    public BaseCrudService init() {
        return billUmLocateDirectService;
    }
}