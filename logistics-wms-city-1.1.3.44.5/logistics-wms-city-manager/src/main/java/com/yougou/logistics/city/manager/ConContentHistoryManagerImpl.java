package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.ConContentHistoryService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-06-18 10:38:59
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
@Service("conContentHistoryManager")
class ConContentHistoryManagerImpl extends BaseCrudManagerImpl implements ConContentHistoryManager {
    @Resource
    private ConContentHistoryService conContentHistoryService;

    @Override
    public BaseCrudService init() {
        return conContentHistoryService;
    }
}