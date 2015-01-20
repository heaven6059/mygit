package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BillOmLoadproposeDtlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-11 18:02:33
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
@Service("billOmLoadproposeDtlManager")
class BillOmLoadproposeDtlManagerImpl extends BaseCrudManagerImpl implements BillOmLoadproposeDtlManager {
    @Resource
    private BillOmLoadproposeDtlService billOmLoadproposeDtlService;

    @Override
    public BaseCrudService init() {
        return billOmLoadproposeDtlService;
    }
}