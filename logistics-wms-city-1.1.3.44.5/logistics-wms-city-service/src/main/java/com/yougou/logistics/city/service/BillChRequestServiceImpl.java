package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BillChRequestMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billChRequestService")
class BillChRequestServiceImpl extends BaseCrudServiceImpl implements BillChRequestService {
    @Resource
    private BillChRequestMapper billChRequestMapper;

    @Override
    public BaseCrudMapper init() {
        return billChRequestMapper;
    }
}