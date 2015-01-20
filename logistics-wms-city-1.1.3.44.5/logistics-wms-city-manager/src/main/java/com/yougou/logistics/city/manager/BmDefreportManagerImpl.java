package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BmDefreportService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bmDefreportManager")
class BmDefreportManagerImpl extends BaseCrudManagerImpl implements BmDefreportManager {
    @Resource
    private BmDefreportService bmDefreportService;

    @Override
    public BaseCrudService init() {
        return bmDefreportService;
    }
}