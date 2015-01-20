package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BillContainerTaskDtlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
@Service("billContainerTaskDtlManager")
class BillContainerTaskDtlManagerImpl extends BaseCrudManagerImpl implements BillContainerTaskDtlManager {
    @Resource
    private BillContainerTaskDtlService billContainerTaskDtlService;

    @Override
    public BaseCrudService init() {
        return billContainerTaskDtlService;
    }
}