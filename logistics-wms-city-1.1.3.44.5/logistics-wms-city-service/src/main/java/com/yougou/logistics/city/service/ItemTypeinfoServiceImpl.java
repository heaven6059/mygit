package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.ItemTypeinfoMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("itemTypeinfoService")
class ItemTypeinfoServiceImpl extends BaseCrudServiceImpl implements ItemTypeinfoService {
    @Resource
    private ItemTypeinfoMapper itemTypeinfoMapper;

    @Override
    public BaseCrudMapper init() {
        return itemTypeinfoMapper;
    }
}