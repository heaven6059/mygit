package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BillChDifferentDtlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billChDifferentDtlManager")
class BillChDifferentDtlManagerImpl extends BaseCrudManagerImpl implements BillChDifferentDtlManager {
    @Resource
    private BillChDifferentDtlService billChDifferentDtlService;

    @Override
    public BaseCrudService init() {
        return billChDifferentDtlService;
    }
}