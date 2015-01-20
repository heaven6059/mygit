package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BillChDifferentMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billChDifferentService")
class BillChDifferentServiceImpl extends BaseCrudServiceImpl implements BillChDifferentService {
    @Resource
    private BillChDifferentMapper billChDifferentMapper;

    @Override
    public BaseCrudMapper init() {
        return billChDifferentMapper;
    }
}