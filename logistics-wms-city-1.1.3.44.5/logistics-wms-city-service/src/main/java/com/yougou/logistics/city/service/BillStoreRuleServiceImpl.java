package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BillStoreRuleMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billStoreRuleService")
class BillStoreRuleServiceImpl extends BaseCrudServiceImpl implements BillStoreRuleService {
    @Resource
    private BillStoreRuleMapper billStoreRuleMapper;

    @Override
    public BaseCrudMapper init() {
        return billStoreRuleMapper;
    }
}