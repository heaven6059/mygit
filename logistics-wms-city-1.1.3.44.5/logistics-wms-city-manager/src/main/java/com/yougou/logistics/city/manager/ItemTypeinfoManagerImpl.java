package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.ItemTypeinfoService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("itemTypeinfoManager")
class ItemTypeinfoManagerImpl extends BaseCrudManagerImpl implements ItemTypeinfoManager {
    @Resource
    private ItemTypeinfoService itemTypeinfoService;

    @Override
    public BaseCrudService init() {
        return itemTypeinfoService;
    }
}