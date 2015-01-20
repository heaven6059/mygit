package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BillChRequestService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billChRequestManager")
class BillChRequestManagerImpl extends BaseCrudManagerImpl implements BillChRequestManager {
    @Resource
    private BillChRequestService billChRequestService;

    @Override
    public BaseCrudService init() {
        return billChRequestService;
    }
}